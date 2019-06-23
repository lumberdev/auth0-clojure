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
Each function corresponds to an Auth0 endpoint. Keyword values like `:auth0.grant-type/authorization-code`
are used for convenience; strings like `"authorization_code"` are also acceptable.
Note that when using keywords hyphens to underscores conversion is done automatically for you.

### URLs

#### /authorize - Authorize

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

#### /v2/logout - Logout

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

##### Log In with Password Realm

Creates a request to log in the user with `username` and `password` using the Password Realm.

```clojure
(oauth-token
    {:auth0/grant-type   "http://auth0.com/oauth/grant-type/password-realm"
     :auth0/realm        "<realm>" ;; like "Username-Password-Authentication"
     :auth0/username     "<username>"
     :auth0/password     "<password>"})
```

##### Client Credentials

Creates a request to get a Token for the given Audience.

```clojure
(oauth-token
    {:auth0/grant-type   :auth0.grant-type/client-credentials
     :auth0/audience     "<audience>"})
```

##### Refresh Token

Use this endpoint to refresh an `Access Token` using the `Refresh Token` you got during authorization.

```clojure
(oauth-token
    {:auth0/grant-type    :auth0.grant-type/refresh-token
     :auth0/refresh-token "<refresh-token>"})
```

#### /oauth/revoke - Revoke Refresh Token

Creates a request to revoke an existing Refresh Token.

```clojure
(oauth-revoke {:auth0/token "<refresh-token>"})
```

#### /dbconnections/signup - Sign Up

Creates a request to create a user.
Up to 10 additional Sign Up fields can be added to the request. This will only work for db connections.

```clojure
;; minimal
(sign-up
    {:auth0/email      "<email>"
     :auth0/password   "<password>"
     :auth0/connection "<connection>" ;; usually "Username-Password-Authentication"
     })

;; all
(sign-up
    {:auth0/email         "<email>"
     :auth0/password      "<password>"
     :auth0/connection    "<connection>" ;; usually "Username-Password-Authentication"
     :auth0/username      "<username>"
     :auth0/given-name    "<first-name>"
     :auth0/family-name   "<last-name>"
     :auth0/name          "<full-name>"
     :auth0/nickname      "<nick>"
     :auth0/picture       "<image-url>"
     :auth0/user-metadata {:some-key "some-val"}})
```

#### /dbconnections/change_password - Reset Password

Creates a request to reset the user's password. This will only work for db connections.

```clojure
(change-password
    {:auth0/email      "<email>"
     :auth0/connection "<connection>" ;; usually "Username-Password-Authentication"})
```

#### /userinfo - User Info

Creates a request to get the user information associated to a given access token.
This will only work if the token has been granted the `openid` scope.

```clojure
(userinfo "<access-token>")
```

## TODO

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
