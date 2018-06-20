import Response from './response';
import * as catalog from './catalog';

export class ResponseError implements Error {
    public name = 'ResponseError';
    public message;
  
    constructor(public response: Response) {
      this.message = response.message
    }
  
    toString = () => `${this.name} ${this.message}`;
}

const create = (response: Response) => (reason) => new ResponseError(Object.assign({}, response, { reason: reason }));

const badParameters = create(catalog.badParameters);
const unauthorizaed = create(catalog.unauthorizaed);
const forbidden = create(catalog.forbidden);
const notFound = create(catalog.notFound);
const wrongServer = create(catalog.wrongServer);

export default {
  badParameters, 
  unauthorizaed, 
  forbidden,
  notFound,
  wrongServer
};