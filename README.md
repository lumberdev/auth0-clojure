# auth0-clojure

Clojure client library for the [Auth0](https://auth0.com)  platform (Authentication API).

## Download

```clojure
[ignorabilis/auth0-clojure "0.1.0-SNAPSHOT"]
```

## Authentication API

The implementation is based on the [Authentication API Docs](https://auth0.com/docs/api/authentication).

Initiate the config by setting the Client (Application) details from the [dashboard](https://manage.auth0.com/#/applications).

```clojure
(ns app.core
  (:require [auth0-clojure.api.authentication :as auth]))

(auth/set-config!
  {:auth0/client-id      "<your-client-id>"
   :auth0/client-secret  "<your-client-secret>"
   :auth0/default-domain "<your-tenant-name>.auth0.com"
   :auth0/custom-domain  "<your-custom-domain>"})
```

Each function then accepts a request settings map and an optional config map, like this:

```clojure
(authorize-url
    {:auth0/response-type "code"
     :auth0/scope         "openid profile"
     :auth0/redirect-uri  "http://localhost:1111/login-user"})

;; OR

(authorize-url
    ;; this one here needs only the client id and the custom or default domain
    {:auth0/client-id      "<your-client-id>"
     :auth0/custom-domain  "<your-custom-domain>"}
    {:auth0/response-type "code"
     :auth0/scope         "openid profile"
     :auth0/redirect-uri  "http://localhost:1111/login-user"})
```

In the samples below only the shorter version will be used.

Each function corresponds to an Auth0 endpoint.

### URLs

#### Authorize - /authorize

Creates an authorize url to authenticate the user with an OAuth provider.
The `:auth0/redirect-uri` must be white-listed in the "Allowed Callback URLs" section
of the Client (Application) Settings. Parameters can be added to the final URL by
adding the values to the map.

```clojure
(authorize-url
    {:auth0/response-type "code"
     :auth0/scope         "openid profile"
     :auth0/redirect-uri  "http://localhost:1111/login-user"})
```

#### Logout - /v2/logout

Creates a logout url to log out the user.
The `auth0/return-to` must be white-listed in the "Allowed Logout URLs" section
of the Dashboard. Parameters can be added to the final URL by adding the values to the map.

```clojure
(logout-url
    {:auth0/return-to "http://localhost:1111/login"
     :auth0/federated true})
```

### Requests

#### /oauth/token

##### Exchange the Authorization Code

Creates a request to exchange the `code` previously obtained by calling the `/authorize` endpoint.
The redirect URI must be the one sent in the `/authorize` call.

```clojure
(oauth-token
    {:auth0/grant-type   :auth0.grant-type/authorization-code
     :auth0/code         "<code>"
     :auth0/redirect-uri "http://localhost:1111/login-user"})
```

##### Log In with Password

Creates a request to log in the user with `username` and `password`.
The connection used is the one defined as "Default Directory" in the account settings.

```clojure
(oauth-token
    {:auth0/grant-type   :auth0.grant-type/password
     :auth0/username     "<username>"
     :auth0/password     "<password>"})
```

## TODO

- Samples
- Authorize url - custom params?
- Logout url - setClientId
- First alpha
- License
- Spec
- Authentication API Passwordless support
- Authentication API MFA support
- Authentication API WS-Federation support
- Handle values like `openid`, `email`, `authorization-code`, etc.; check the OpenID standards for an exhaustive list
- Detailed samples
- Management API
- Default utility that handles Management API token refreshes
- Tests

## License

Copyright © 2019 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
