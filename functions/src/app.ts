import * as functions from 'firebase-functions';
import * as express from 'express';

import Action from './action';
import authorize from './auth/authorize';
import error, { ResponseError } from './response/error';

// A wrapper of express. 

interface ResponseHandler {
    (request: functions.Request, handler: functions.Response): void
}

class Router<T extends express.Router> {
    constructor(protected expressRouter: T) {
    }

    public get(endpoint: string, action: Action) {
        this.expressRouter.get(endpoint, wrapCallback(action));
    }

    public post(endpoint: string, action: Action) {
        this.expressRouter.post(endpoint, wrapCallback(action));
    }

    public use<U extends express.Router>(r: Router<U>) {
        this.expressRouter.use(r.expressRouter);
    }
}

class RouterSelf extends Router<express.Router> {
    constructor() {
        super(express.Router());
    }
}

class App extends Router<express.Express> {
    public responseHandler: ResponseHandler;

    constructor(public needAuthorize: boolean) {
        super(express());

        this.responseHandler = (request, handler) => {
            // a workaround
            if (!request.url || request.url[0] !== '/') {
                request.url = '/' + request.url;
                request.path = '/' + request.path;
            }
        
            if (needAuthorize) {
                return authorize(request).then((_) => this.expressRouter(request, handler));
            } else {
                return this.expressRouter(request, handler);
            }
        }
    }
}

function wrapCallback(action: Action) {
    return (request, handler) => {
        action(request)
            .then((response) => handler.status(response.status).send(response))
            .catch((e) => {
                if (!(e instanceof ResponseError)) {
                    console.log('An uncaught error', e);
                    const err = error.wrongServer(e);
                    handler.status(err.response.status).send(err.response);
                } else {
                    handler.status(e.response.status).send(e.response);
                }
            });
    }
}

export function app(needAuthorize: boolean){
    return new App(needAuthorize);
}

export function router(): Router<express.Router> {
    return new RouterSelf()
}