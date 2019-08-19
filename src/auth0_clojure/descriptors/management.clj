(ns auth0-clojure.descriptors.management)

(def api-descriptor
  {:version    "2.0"
   :metadata   {:endpoint-prefix "/api/v2/"}
   :operations {:get-connections      {:name    :get-connections
                                       :doc     "Retrieves every connection matching the specified strategy. All connections are retrieved if no strategy is being specified. Accepts a list of fields to include or exclude in the resulting list of connection objects."
                                       :doc-url "https://auth0.com/docs/api/management/v2#!/Connections/get_connections"
                                       :http    {:path          ["connections"]
                                                 :method        :get
                                                 :response-code 200}}
                :create-connection    {:name    :create-connection
                                       :doc     "Creates a new connection according to the JSON object received in body. Valid Strategy names are: ad, adfs, amazon, apple, dropbox, bitbucket, aol, auth0-adldap, auth0-oidc, auth0, baidu, bitly, box, custom, daccount, dwolla, email, evernote-sandbox, evernote, exact, facebook, fitbit, flickr, github, google-apps, google-oauth2, instagram, ip, linkedin, miicard, oauth1, oauth2, office365, oidc, paypal, paypal-sandbox, pingfederate, planningcenter, renren, salesforce-community, salesforce-sandbox, salesforce, samlp, sharepoint, shopify, sms, soundcloud, thecity-sandbox, thecity, thirtysevensignals, twitter, untappd, vkontakte, waad, weibo, windowslive, wordpress, yahoo, yammer, yandex, line."
                                       :doc-url "https://auth0.com/docs/api/management/v2#!/Connections/post_connections"
                                       :http    {:path          ["connections"]
                                                 :method        :post
                                                 :response-code 201}}
                :get-connection       {:name    :get-connection
                                       :doc     "Retrieves a connection by its ID."
                                       :doc-url "https://auth0.com/docs/api/management/v2#!/Connections/get_connections_by_id"
                                       :http    {:path          ["connections" :id]
                                                 :method        :get
                                                 :response-code 200}}
                :delete-connection    {:name    :delete-connection
                                       :doc     "Deletes a connection and all its users."
                                       :doc-url "https://auth0.com/docs/api/management/v2#!/Connections/delete_connections_by_id"
                                       :http    {:path          ["connections" :id]
                                                 :method        :delete
                                                 :response-code 204}}
                :update-connection    {:name    :update-connection
                                       :doc     "Updates a connection."
                                       :doc-url "https://auth0.com/docs/api/management/v2#!/Connections/patch_connections_by_id"
                                       :http    {:path          ["connections" :id]
                                                 :method        :patch
                                                 :response-code 200}}
                :delete-user-by-email {:name    :delete-user-by-email
                                       :doc     "Deletes a specified connection user by its email (you cannot delete all users from specific connection). Currently, only Database Connections are supported."
                                       :doc-url "https://auth0.com/docs/api/management/v2#!/Connections/delete_users_by_email"
                                       :http    {:path          ["connections" :id "users"]
                                                 :method        :delete
                                                 :response-code 204}}}})
