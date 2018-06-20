import Response from './response';

export const ok: Response = {
    success: true,
    message: 'OK',
    status: 200
};

export const created: Response = {
    success: true,
    message: 'Created',
    status: 201
};

export const badParameters: Response = {
    success: false, 
    message: 'Bad parameters',
    status: 400
};

export const unauthorizaed: Response = {
    success: false, 
    message: 'Unauthorized',
    status: 401
};

export const forbidden: Response = {
    success: false, 
    message: 'Forbidden',
    status: 403
};

export const notFound: Response = {
    success: false, 
    message: 'Not found',
    status: 404
};

export const wrongServer: Response = {
    success: false, 
    message: 'Found a server error',
    status: 500
};