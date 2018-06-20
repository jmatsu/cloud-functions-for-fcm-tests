import * as AuthProvider from './provider/firebaseAuthentation';
import AuthGateway from './gateway';

// You can implement other authentication provider
const authorize: AuthGateway = AuthProvider.authorize;

export default authorize;