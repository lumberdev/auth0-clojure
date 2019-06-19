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

## TODO

- Samples
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
