(ns auth0-clojure.descriptors.management)

;; TODO - better docs
(def api-descriptor
  {:version    "2.0"
   :metadata   {:endpoint-prefix "/api/v2/"}
   :operations {
                ;; Branding
                :get-branding-settings             {:name    :get-branding-settings
                                                    :doc     "Use this endpoint to retrieve various settings related to branding."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Branding/get_branding"
                                                    :http    {:path          ["branding"]
                                                              :method        :get
                                                              :response-code 200}}
                :update-branding-settings          {:name    :update-branding-settings
                                                    :doc     "Use this endpoint to update various fields related to branding."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Branding/patch_branding"
                                                    :http    {:path          ["branding"]
                                                              :method        :patch
                                                              :response-code 200}}

                ;; Client Grants
                :get-client-grants                 {:name    :get-client-grants
                                                    :doc     "Retrieve client grants."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Client_Grants/get_client_grants"
                                                    :http    {:path          ["client-grants"]
                                                              :method        :get
                                                              :response-code 200}}
                :create-client-grant               {:name    :create-client-grant
                                                    :doc     "Create a client grant."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Client_Grants/post_client_grants"
                                                    :http    {:path          ["client-grants"]
                                                              :method        :post
                                                              :response-code 201}}
                :update-client-grant               {:name    :update-client-grant
                                                    :doc     "Update a client grant."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Client_Grants/patch_client_grants_by_id"
                                                    :http    {:path          ["client-grants" :id]
                                                              :method        :patch
                                                              :response-code 200}}
                :delete-client-grant               {:name    :delete-client-grant
                                                    :doc     "Delete a client grant."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Client_Grants/delete_client_grants_by_id"
                                                    :http    {:path          ["client-grants" :id]
                                                              :method        :delete
                                                              :response-code 204}}

                ;;Clients
                :get-clients                       {:name    :get-clients
                                                    :doc     "Retrieves a list of all client applications. Accepts a list of fields to include or exclude. Important: The client_secret and encryption_key attributes can only be retrieved with the read:client_keys scope."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Clients/get_clients"
                                                    :http    {:path          ["clients"]
                                                              :method        :get
                                                              :response-code 200}}
                :create-client                     {:name    :create-client
                                                    :doc     "Creates a new client application. The samples on the right show most attributes that can be used. We recommend to let us to generate a safe secret for you, but you can also provide your own with the client_secret parameter"
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Clients/post_clients"
                                                    :http    {:path          ["clients"]
                                                              :method        :post
                                                              :response-code 201}}
                :get-client                        {:name    :get-client
                                                    :doc     "Retrieves a client by its id. Important: The client_secret encryption_key and signing_keys attributes can only be retrieved with the read:client_keys scope."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Clients/get_clients_by_id"
                                                    :http    {:path          ["clients" :id]
                                                              :method        :get
                                                              :response-code 200}}
                :update-client                     {:name    :update-client
                                                    :doc     "Important: The client_secret and encryption_key attributes can only be updated with the update:client_keys scope."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Clients/patch_clients_by_id"
                                                    :http    {:path          ["clients" :id]
                                                              :method        :patch
                                                              :response-code 200}}
                :delete-client                     {:name    :delete-client
                                                    :doc     "Deletes a client and all its related assets (like rules, connections, etc) given its id."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Clients/delete_clients_by_id"
                                                    :http    {:path          ["clients" :id]
                                                              :method        :delete
                                                              :response-code 204}}
                :rotate-client-secret              {:name    :rotate-client-secret
                                                    :doc     "Rotate a client secret. The generated secret is NOT base64 encoded."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Clients/post_rotate_secret"
                                                    :http    {:path          ["clients" :id "rotate-secret"]
                                                              :method        :post
                                                              :response-code 200}}

                ;; Connections
                :get-connections                   {:name    :get-connections
                                                    :doc     "Retrieves every connection matching the specified strategy. All connections are retrieved if no strategy is being specified. Accepts a list of fields to include or exclude in the resulting list of connection objects."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Connections/get_connections"
                                                    :http    {:path          ["connections"]
                                                              :method        :get
                                                              :response-code 200}}
                :create-connection                 {:name    :create-connection
                                                    :doc     "Creates a new connection according to the JSON object received in body. Valid Strategy names are: ad, adfs, amazon, apple, dropbox, bitbucket, aol, auth0-adldap, auth0-oidc, auth0, baidu, bitly, box, custom, daccount, dwolla, email, evernote-sandbox, evernote, exact, facebook, fitbit, flickr, github, google-apps, google-oauth2, instagram, ip, linkedin, miicard, oauth1, oauth2, office365, oidc, paypal, paypal-sandbox, pingfederate, planningcenter, renren, salesforce-community, salesforce-sandbox, salesforce, samlp, sharepoint, shopify, sms, soundcloud, thecity-sandbox, thecity, thirtysevensignals, twitter, untappd, vkontakte, waad, weibo, windowslive, wordpress, yahoo, yammer, yandex, line."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Connections/post_connections"
                                                    :http    {:path          ["connections"]
                                                              :method        :post
                                                              :response-code 201}}
                :get-connection                    {:name    :get-connection
                                                    :doc     "Retrieves a connection by its ID."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Connections/get_connections_by_id"
                                                    :http    {:path          ["connections" :id]
                                                              :method        :get
                                                              :response-code 200}}
                :update-connection                 {:name    :update-connection
                                                    :doc     "Updates a connection."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Connections/patch_connections_by_id"
                                                    :http    {:path          ["connections" :id]
                                                              :method        :patch
                                                              :response-code 200}}
                :delete-connection                 {:name    :delete-connection
                                                    :doc     "Deletes a connection and all its users."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Connections/delete_connections_by_id"
                                                    :http    {:path          ["connections" :id]
                                                              :method        :delete
                                                              :response-code 204}}
                :delete-user-by-email              {:name    :delete-user-by-email
                                                    :doc     "Deletes a specified connection user by its email (you cannot delete all users from specific connection). Currently, only Database Connections are supported."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Connections/delete_users_by_email"
                                                    :http    {:path          ["connections" :id "users"]
                                                              :method        :delete
                                                              :response-code 204}}

                ;; Custom Domains
                :get-custom-domains                {:name    :get-custom-domains
                                                    :doc     "Retrieves details on custom domains."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Custom_Domains/get_custom_domains"
                                                    :http    {:path          ["custom-domains"]
                                                              :method        :get
                                                              :response-code 200}}
                :config-new-custom-domain          {:name    :config-new-custom-domain
                                                    :doc     "Create a new custom domain."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Custom_Domains/post_custom_domains"
                                                    :http    {:path          ["custom-domains"]
                                                              :method        :post
                                                              :response-code 201}}
                :get-custom-domain                 {:name    :get-custom-domain
                                                    :doc     "Retrieve a custom domain configuration and status."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Custom_Domains/get_custom_domains_by_id"
                                                    :http    {:path          ["custom-domains" :id]
                                                              :method        :get
                                                              :response-code 200}}
                :verify-custom-domain              {:name    :verify-custom-domain
                                                    :doc     "Run the verification process on a custom domain. Note: Check the `status` field to see its verification status. Once verification is complete, it may take up to 10 minutes before the custom domain can start accepting requests."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Custom_Domains/post_verify"
                                                    :http    {:path          ["custom-domains" :id "verify"]
                                                              :method        :post
                                                              :response-code 200}}
                :delete-custom-domain-config       {:name    :delete-custom-domain-config
                                                    :doc     "Delete a custom domain and stop serving requests for it."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Custom_Domains/delete_custom_domains_by_id"
                                                    :http    {:path          ["custom-domains" :id]
                                                              :method        :delete
                                                              :response-code 204}}

                ;; Device Credentials
                :get-device-credentials            {:name    :get-device-credentials
                                                    :doc     "Manage the devices that are recognized and authenticated. Please note that Device Credentials endpoints are designed for ad hoc administrative use only."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Device_Credentials/get_device_credentials"
                                                    :http    {:path          ["device-credentials"]
                                                              :method        :get
                                                              :response-code 200}}
                :create-device-public-key          {:name    :create-device-public-key
                                                    :doc     "Manage the devices that are recognized and authenticated. Please note that Device Credentials endpoints are designed for ad hoc administrative use only."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Device_Credentials/post_device_credentials"
                                                    :http    {:path          ["device-credentials"]
                                                              :method        :post
                                                              :response-code 201}}
                :delete-device-public-key          {:name    :delete-device-public-key
                                                    :doc     "Manage the devices that are recognized and authenticated. Please note that Device Credentials endpoints are designed for ad hoc administrative use only."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Device_Credentials/delete_device_credentials_by_id"
                                                    :http    {:path          ["device-credentials" :id]
                                                              :method        :delete
                                                              :response-code 204}}

                ;; Grants
                :get-grants                        {:name    :get-grants
                                                    :doc     "Manage the grants associated with your account."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Grants/get_grants"
                                                    :http    {:path          ["grants"]
                                                              :method        :get
                                                              :response-code 200}}
                :delete-grant                      {:name    :delete-grant
                                                    :doc     ""
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Grants/delete_grants_by_id"
                                                    :http    {:path          ["grants" :id]
                                                              :method        :delete
                                                              :response-code 204}}

                ;; Logs
                :search-log-events                 {:name    :search-log-events
                                                    :doc     "Retrieves log entries that match the specified search criteria (or lists all log entries if no criteria are used). Set custom search criteria using the q parameter, or search from a specific log ID (\"search from checkpoint\")."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Logs/get_logs"
                                                    :http    {:path          ["logs"]
                                                              :method        :get
                                                              :response-code 200}}
                :get-log-event                     {:name    :get-log-event
                                                    :doc     "Retrieves the data related to the log entry identified by id. This returns a single log entry representation as specified in the schema."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Logs/get_logs_by_id"
                                                    :http    {:path          ["logs" :id]
                                                              :method        :get
                                                              :response-code 200}}

                ;; Prompts
                :get-prompt-settings               {:name    :get-prompt-settings
                                                    :doc     "Retrieve prompts settings."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Prompts/get_prompts"
                                                    :http    {:path          ["prompts"]
                                                              :method        :get
                                                              :response-code 200}}
                :update-prompt-settings            {:name    :update-prompt-settings
                                                    :doc     "Update prompts settings."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Prompts/patch_prompts"
                                                    :http    {:path          ["prompts"]
                                                              :method        :patch
                                                              :response-code 200}}

                ;; Roles
                :get-roles                         {:name    :get-roles
                                                    :doc     "Lists all the roles."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Roles/get_roles"
                                                    :http    {:path          ["roles"]
                                                              :method        :get
                                                              :response-code 200}}
                :create-role                       {:name    :create-role
                                                    :doc     "Creates a new role."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Roles/post_roles"
                                                    :http    {:path          ["roles"]
                                                              :method        :post
                                                              :response-code 200}}
                :get-role                          {:name    :get-role
                                                    :doc     "Gets the information for a role."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Roles/get_roles_by_id"
                                                    :http    {:path          ["roles" :id]
                                                              :method        :get
                                                              :response-code 200}}
                :update-role                       {:name    :update-role
                                                    :doc     "Updates a role with new values."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Roles/patch_roles_by_id"
                                                    :http    {:path          ["roles" :id]
                                                              :method        :patch
                                                              :response-code 200}}
                :delete-role                       {:name    :delete-role
                                                    :doc     "Deletes a role used in authorization of users against resource servers."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Roles/delete_roles_by_id"
                                                    :http    {:path          ["roles" :id]
                                                              :method        :delete
                                                              :response-code 204}}
                :get-role-permissions              {:name    :get-role-permissions
                                                    :doc     "Gets the permissions for a role."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Roles/get_role_permission"
                                                    :http    {:path          ["roles" :id "permissions"]
                                                              :method        :get
                                                              :response-code 200}}
                :associate-role-permissions        {:name    :associate-role-permissions
                                                    :doc     "Associates permissions with a role."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Roles/post_role_permission_assignment"
                                                    :http    {:path          ["roles" :id "permissions"]
                                                              :method        :post
                                                              :response-code 201}}
                :unassociate-role-permissions      {:name    :unassociate-role-permissions
                                                    :doc     "Unassociates permissions from a role."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Roles/delete_role_permission_assignment"
                                                    :http    {:path          ["roles" :id "permissions"]
                                                              :method        :delete
                                                              ;; TODO-descriptor - shouldn't this be 204???
                                                              :response-code 200}}
                :get-role-users                    {:name    :get-role-users
                                                    :doc     "Lists the users that have been associated with a given role."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Roles/get_role_user"
                                                    :http    {:path          ["roles" :id "users"]
                                                              :method        :get
                                                              :response-code 200}}
                :assign-role-users                 {:name    :assign-role-users
                                                    :doc     "Assign users to a role."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Roles/post_role_users"
                                                    :http    {:path          ["roles" :id "users"]
                                                              :method        :post
                                                              :response-code 200}}

                ;; Rules
                :get-rules                         {:name    :get-rules
                                                    :doc     "Retrieves a list of all rules. Accepts a list of fields to include or exclude."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Rules/get_rules"
                                                    :http    {:path          ["rules"]
                                                              :method        :get
                                                              :response-code 200}}
                :create-rule                       {:name    :create-rule
                                                    :doc     "Creates a new rule according to the JSON object received in body."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Rules/post_rules"
                                                    :http    {:path          ["rules"]
                                                              :method        :post
                                                              :response-code 201}}
                :get-rule                          {:name    :get-rule
                                                    :doc     "Retrieves a rule by its ID. Accepts a list of fields to include or exclude in the result."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Rules/get_rules_by_id"
                                                    :http    {:path          ["rules" :id]
                                                              :method        :get
                                                              :response-code 200}}
                :update-rule                       {:name    :update-rule
                                                    :doc     "Use this endpoint to update an existing rule."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Rules/patch_rules_by_id"
                                                    :http    {:path          ["rules" :id]
                                                              :method        :patch
                                                              :response-code 200}}
                :delete-rule                       {:name    :delete-rule
                                                    :doc     "To be used to delete a rule."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Rules/delete_rules_by_id"
                                                    :http    {:path          ["rules" :id]
                                                              :method        :delete
                                                              :response-code 204}}

                ;; Rules Configs
                :get-rules-configs                 {:name    :get-rules-configs
                                                    :doc     "Returns only rules config variable keys. For security, config variable values cannot be retrieved outside rule execution"
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Rules_Configs/get_rules_configs"
                                                    :http    {:path          ["rules-configs"]
                                                              :method        :get
                                                              :response-code 200}}
                :set-rules-config                  {:name    :set-rules-config
                                                    :doc     "Rules config keys must be of the format ^[A-Za-z0-9_\\-@*+:]*$."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Rules_Configs/put_rules_configs_by_key"
                                                    :http    {:path          ["rules-configs" :key]
                                                              :method        :put
                                                              :response-code 200}}
                :delete-rules-config               {:name    :delete-rules-config
                                                    :doc     "Rules config keys must be of the format ^[A-Za-z0-9_\\-@*+:]*$."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Rules_Configs/delete_rules_configs_by_key"
                                                    :http    {:path          ["rules-configs" :key]
                                                              :method        :delete
                                                              :response-code 204}}

                ;; User Blocks
                :get-user-blocks                   {:name    :get-user-blocks
                                                    :doc     "This endpoint can be used to retrieve a list of blocked IP addresses of a particular user given a user_id."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/User_Blocks/get_user_blocks_by_id"
                                                    :http    {:path          ["user-blocks" :id]
                                                              :method        :get
                                                              :response-code 200}}
                :unblock-user                      {:name    :unblock-user
                                                    :doc     "This endpoint can be used to unblock a user that was blocked due to an excessive amount of incorrectly provided credentials."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/User_Blocks/delete_user_blocks_by_id"
                                                    :http    {:path          ["user-blocks" :id]
                                                              :method        :delete
                                                              :response-code 204}}
                :get-user-blocks-by-identifier     {:name    :get-user-blocks-by-identifier
                                                    :doc     "This endpoint can be used to retrieve a list of blocked IP addresses for a given key."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/User_Blocks/get_user_blocks"
                                                    :http    {:path          ["user-blocks"]
                                                              :method        :get
                                                              :response-code 200}}
                :unblock-user-by-identifier        {:name    :unblock-user-by-identifier
                                                    :doc     "This endpoint can be used to unblock a given key that was blocked due to an excessive amount of incorrectly provided credentials."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/User_Blocks/delete_user_blocks"
                                                    :http    {:path          ["user-blocks"]
                                                              :method        :delete
                                                              :response-code 204}}

                ;; Users
                :get-users                         {:name    :get-users
                                                    :doc     "This endpoint can be used to retrieve a list of users."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Users/get_users"
                                                    :http    {:path          ["users"]
                                                              :method        :get
                                                              :response-code 200}}
                :get-users-by-email                {:name    :get-users-by-email
                                                    :doc     "If Auth0 is the identify provider (idP), the email address associated with a user is saved in lower case, regardless of how you initially provided it. For example, if you register a user as JohnSmith@example.com, Auth0 saves the user's email as johnsmith@example.com. In cases where Auth0 is not the idP, the `email` is stored based on the rules of idP, so make sure the search is made using the correct capitalization."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Users_By_Email/get_users_by_email"
                                                    :http    {:path          ["users-by-email"]
                                                              :method        :get
                                                              :response-code 200}}
                :create-user                       {:name    :create-user
                                                    :doc     "Creates a new user according to the JSON object received in body. It works only for database and passwordless connections."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Users/post_users"
                                                    :http    {:path          ["users"]
                                                              :method        :post
                                                              :response-code 201}}
                :get-user                          {:name    :get-user
                                                    :doc     "This endpoint can be used to retrieve user details given the user_id."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Users/get_users_by_id"
                                                    :http    {:path          ["users" :id]
                                                              :method        :get
                                                              :response-code 200}}
                :update-user                       {:name    :update-user
                                                    :doc     "Updates a user with the object's properties received in the request's body (the object should be a JSON object)."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Users/patch_users_by_id"
                                                    :http    {:path          ["users" :id]
                                                              :method        :patch
                                                              :response-code 200}}
                :delete-user                       {:name    :delete-user
                                                    :doc     "This endpoint can be used to delete a single user based on the id."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Users/delete_users_by_id"
                                                    :http    {:path          ["users" :id]
                                                              :method        :delete
                                                              :response-code 204}}
                :get-user-roles                    {:name    :get-user-roles
                                                    :doc     "List the the roles associated with a user."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Users/get_user_roles"
                                                    :http    {:path          ["users" :id "roles"]
                                                              :method        :get
                                                              :response-code 200}}
                :assign-user-roles                 {:name    :assign-user-roles
                                                    :doc     "Associate an array of roles with a user."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Users/post_user_roles"
                                                    :http    {:path          ["users" :id "roles"]
                                                              :method        :post
                                                              :response-code 201}}
                :unassign-user-roles               {:name    :unassign-user-roles
                                                    :doc     "Removes an array of roles from a user."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Users/delete_user_roles"
                                                    :http    {:path          ["users" :id "roles"]
                                                              :method        :delete
                                                              :response-code 204}}
                :get-user-log-events               {:name    :get-user-log-events
                                                    :doc     "Retrieve every log event for a specific user id."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Users/get_logs_by_user"
                                                    :http    {:path          ["users" :id "logs"]
                                                              :method        :get
                                                              :response-code 200}}
                :get-user-guardian-enrollments     {:name    :get-user-guardian-enrollments
                                                    :doc     "Retrieves all Guardian enrollments."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Users/get_enrollments"
                                                    :http    {:path          ["users" :id "enrollments"]
                                                              :method        :get
                                                              :response-code 200}}
                :generate-guardian-recovery-code   {:name    :generate-guardian-recovery-code
                                                    :doc     "This endpoint removes the current Guardian recovery code then generates and returns a new one."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Users/post_recovery_code_regeneration"
                                                    :http    {:path          ["users" :id "recovery-code-generation"]
                                                              :method        :post
                                                              :response-code 200}}
                :get-user-permissions              {:name    :get-user-permissions
                                                    :doc     "Get the permissions associated to the user."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Users/get_permissions"
                                                    :http    {:path          ["users" :id "permissions"]
                                                              :method        :get
                                                              :response-code 200}}
                :assign-user-permissions           {:name    :assign-user-permissions
                                                    :doc     "This endpoint assigns the permission specified in the request payload to the user defined in the path."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Users/post_permissions"
                                                    :http    {:path          ["users" :id "permissions"]
                                                              :method        :post
                                                              :response-code 201}}
                :unassign-user-permissions         {:name    :unassign-user-permissions
                                                    :doc     "Removes permissions from a user."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Users/delete_permissions"
                                                    :http    {:path          ["users" :id "permissions"]
                                                              :method        :delete
                                                              :response-code 204}}
                :delete-user-multifactor-provider  {:name    :delete-user-multifactor-provider
                                                    :doc     "This endpoint can be used to delete the multifactor provider settings for a particular user. This will force user to re-configure the multifactor provider."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Users/delete_multifactor_by_provider"
                                                    :http    {:path          ["users" :id "multifactor" :provider]
                                                              :method        :delete
                                                              :response-code 204}}
                :link-user-account                 {:name    :link-user-account
                                                    :doc     "Links the account specified in the body (secondary account) to the account specified by the id param of the URL (primary account)."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Users/post_identities"
                                                    :http    {:path          ["users" :id "identities"]
                                                              :method        :post
                                                              :response-code 201}}
                :unlink-user-account               {:name    :unlink-user-account
                                                    :doc     "Unlinks an identity from the target user, and it becomes a separated user again."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Users/delete_user_identity_by_user_id"
                                                    :http    {:path          ["users" :id "identities" :provider :user-id]
                                                              :method        :delete
                                                              :response-code 204}}
                :invalidate-user-mfa-browsers      {:name    :invalidate-user-mfa-browsers
                                                    :doc     "This endpoint invalidates all remembered browsers for all authentication factors for the selected user."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Users/post_invalidate_remember_browser"
                                                    :http    {:path          ["users" :id "multifactor" "actions" "invalidate-remember-browser"]
                                                              :method        :post
                                                              :response-code 204}}

                ;; Blacklists
                :get-blacklisted-tokens            {:name    :get-blacklisted-tokens
                                                    :doc     "Retrieve the `jti` and `aud` of all tokens that are blacklisted."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Blacklists/get_tokens"
                                                    :http    {:path          ["blacklists" "tokens"]
                                                              :method        :get
                                                              :response-code 200}}
                :blacklist-token                   {:name    :blacklist-token
                                                    :doc     "Add the token identified by the `jti` to a blacklist for the tenant."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Blacklists/post_tokens"
                                                    :http    {:path          ["blacklists" "tokens"]
                                                              :method        :post
                                                              :response-code 204}}

                ;; Email Templates
                :create-email-template             {:name    :create-email-template
                                                    :doc     ""
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Email_Templates/post_email_templates"
                                                    :http    {:path          ["email-templates"]
                                                              :method        :post
                                                              :response-code 200}}
                :get-email-template                {:name    :get-email-template
                                                    :doc     ""
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Email_Templates/get_email_templates_by_templateName"
                                                    :http    {:path          ["email-templates" :template-name]
                                                              :method        :get
                                                              :response-code 200}}
                :update-email-template             {:name    :update-email-template
                                                    :doc     ""
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Email_Templates/patch_email_templates_by_templateName"
                                                    :http    {:path          ["email-templates" :template-name]
                                                              :method        :patch
                                                              :response-code 200}}
                :set-email-template                {:name    :set-email-template
                                                    :doc     ""
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Email_Templates/put_email_templates_by_templateName"
                                                    :http    {:path          ["email-templates" :template-name]
                                                              :method        :put
                                                              :response-code 200}}

                ;; Emails
                :create-email-provider             {:name    :create-email-provider
                                                    :doc     "To be used to set a new email provider."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Emails/post_provider"
                                                    :http    {:path          ["emails" "provider"]
                                                              :method        :post
                                                              :response-code 200}}
                :get-email-provider                {:name    :get-email-provider
                                                    :doc     "This endpoint can be used to retrieve the current email provider configuration."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Emails/get_provider"
                                                    :http    {:path          ["emails" "provider"]
                                                              :method        :get
                                                              :response-code 200}}
                :update-email-provider             {:name    :update-email-provider
                                                    :doc     "Can be used to change details for an email provider.\n"
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Emails/patch_provider"
                                                    :http    {:path          ["emails" "provider"]
                                                              :method        :patch
                                                              :response-code 200}}
                :delete-email-provider             {:name    :delete-email-provider
                                                    :doc     ""
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Emails/delete_provider"
                                                    :http    {:path          ["emails" "provider"]
                                                              :method        :delete
                                                              :response-code 204}}

                ;; Guardian
                :get-guardian-factors              {:name    :get-guardian-factors
                                                    :doc     "Retrieves all factors. Useful to check factor enablement and trial status."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Guardian/get_factors"
                                                    :http    {:path          ["guardian" "factors"]
                                                              :method        :get
                                                              :response-code 200}}
                :set-guardian-factor               {:name    :set-guardian-factor
                                                    :doc     "Useful to enable / disable factor."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Guardian/put_factors_by_name"
                                                    :http    {:path          ["guardian" "factors" :name]
                                                              :method        :put
                                                              :response-code 200}}
                :create-guardian-enrollment-ticket {:name    :create-guardian-enrollment-ticket
                                                    :doc     "Generate an email with a link to start the Guardian enrollment process."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Guardian/post_ticket"
                                                    :http    {:path          ["guardian" "enrollments" "ticket"]
                                                              :method        :post
                                                              :response-code 204}}
                :get-guardian-enrollment           {:name    :get-guardian-enrollment
                                                    :doc     "Retrieves an enrollment. Useful to check its type and related metadata. Note that phone number data is partially obfuscated."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Guardian/get_enrollments_by_id"
                                                    :http    {:path          ["guardian" "enrollments" :id]
                                                              :method        :get
                                                              :response-code 200}}
                :delete-guardian-enrollment        {:name    :delete-guardian-enrollment
                                                    :doc     "Deletes an enrollment. Useful when you want to force the user to re-enroll with Guardian."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Guardian/delete_enrollments_by_id"
                                                    :http    {:path          ["guardian" "enrollments" :id]
                                                              :method        :delete
                                                              :response-code 200}}
                :get-guardian-factor-templates     {:name    :get-guardian-factor-templates
                                                    :doc     "Retrieves enrollment and verification templates. You can use this to check the current values for your templates."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Guardian/get_templates"
                                                    :http    {:path          ["guardian" "factors" "sms" "templates"]
                                                              :method        :get
                                                              :response-code 200}}
                :update-guardian-factor-templates  {:name    :update-guardian-factor-templates
                                                    :doc     "Useful to send custom messages on SMS enrollment and verification."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Guardian/put_templates"
                                                    :http    {:path          ["guardian" "factors" "sms" "templates"]
                                                              :method        :put
                                                              :response-code 200}}
                :get-guardian-factor-sns           {:name    :get-guardian-factor-sns
                                                    :doc     "Returns provider configuration for AWS SNS."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Guardian/get_sns"
                                                    :http    {:path          ["guardian" "factors" "push-notification" "providers" "sns"]
                                                              :method        :get
                                                              :response-code 200}}
                :set-guardian-factor-sns           {:name    :set-guardian-factor-sns
                                                    :doc     "Useful to configure the push notification provider."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Guardian/put_sns"
                                                    :http    {:path          ["guardian" "factors" "push-notification" "providers" "sns"]
                                                              :method        :put
                                                              :response-code 200}}
                :get-guardian-factor-twilio        {:name    :get-guardian-factor-twilio
                                                    :doc     "Returns provider configuration."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Guardian/get_twilio"
                                                    :http    {:path          ["guardian" "factors" "push-notification" "providers" "twilio"]
                                                              :method        :get
                                                              :response-code 200}}
                :set-guardian-factor-twilio        {:name    :set-guardian-factor-twilio
                                                    :doc     "Useful to configure SMS provider."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Guardian/put_twilio"
                                                    :http    {:path          ["guardian" "factors" "push-notification" "providers" "twilio"]
                                                              :method        :put
                                                              :response-code 200}}

                ;; Jobs
                :get-job                           {:name    :get-job
                                                    :doc     "Retrieves a job. Useful to check its status."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Jobs/get_jobs_by_id"
                                                    :http    {:path          ["jobs" :id]
                                                              :method        :get
                                                              :response-code 200}}
                :get-job-errors                    {:name    :get-job-errors
                                                    :doc     "Retrieve error details of a failed job."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Jobs/get_errors"
                                                    :http    {:path          ["jobs" :id "errors"]
                                                              :method        :get
                                                              :response-code 200}}
                :get-job-results                   {:name    :get-job-results
                                                    :doc     ""
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Jobs/get_results"
                                                    :http    {:path          ["jobs" :id "results"]
                                                              :method        :get
                                                              :response-code 200}}
                :create-export-users-job           {:name    :create-export-users-job
                                                    :doc     "Export all users to a file via a long-running job."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Jobs/post_users_exports"
                                                    :http    {:path          ["jobs" "users-exports"]
                                                              :method        :post
                                                              :response-code 201}}
                :create-import-users-job           {:name    :create-import-users-job
                                                    :doc     "Import users from a formatted file into a connection via a long-running job."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Jobs/post_users_imports"
                                                    :http    {:path          ["jobs" "users-imports"]
                                                              :method        :post
                                                              :response-code 201}}
                :send-verification-email           {:name    :send-verification-email
                                                    :doc     "Send an email to the specified user that asks them to click a link to verify their email address."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Jobs/post_verification_email"
                                                    :http    {:path          ["jobs" "verification-email"]
                                                              :method        :post
                                                              :response-code 201}}

                ;; Stats
                :get-active-users-count            {:name    :get-active-users-count
                                                    :doc     "Retrieve the number of active users that logged in during the last 30 days."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Stats/get_active_users"
                                                    :http    {:path          ["stats" "active-users"]
                                                              :method        :get
                                                              :response-code 200}}
                :get-daily-stats                   {:name    :get-daily-stats
                                                    :doc     "Retrieve the number of logins, signups and breached-password detections (subscription required) that occurred each day within a specified date range."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Stats/get_daily"
                                                    :http    {:path          ["stats" "daily"]
                                                              :method        :get
                                                              :response-code 200}}

                ;; Tenants
                :get-tenant-settings               {:name    :get-tenant-settings
                                                    :doc     "Use this endpoint to retrieve various settings for a tenant."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Tenants/get_settings"
                                                    :http    {:path          ["tenants" "settings"]
                                                              :method        :get
                                                              :response-code 200}}
                :update-tenant-settings            {:name    :update-tenant-settings
                                                    :doc     "Use this endpoint to update various fields for a tenant. Enter the new settings in a JSON string in the body parameter."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Tenants/patch_settings"
                                                    :http    {:path          ["tenants" "settings"]
                                                              :method        :patch
                                                              :response-code 200}}

                ;; Anomaly
                :check-if-ip-blocked               {:name    :check-if-ip-blocked
                                                    :doc     "Check if a given IP address is blocked via the multiple user accounts trigger due to multiple failed logins."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Anomaly/get_ips_by_id"
                                                    :http    {:path          ["anomaly" "blocks" "ips" :id]
                                                              :method        :get
                                                              :response-code 200}}
                :unblock-ip                        {:name    :unblock-ip
                                                    :doc     "Unblock an IP address currently blocked by the multiple user accounts trigger due to multiple failed logins."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Anomaly/delete_ips_by_id"
                                                    :http    {:path          ["anomaly" "blocks" "ips" :id]
                                                              :method        :delete
                                                              :response-code 204}}

                ;; Tickets
                :create-email-verification-ticket  {:name    :create-email-verification-ticket
                                                    :doc     "Create a ticket to verify a user's email address."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Tickets/post_email_verification"
                                                    :http    {:path          ["tickets" "email-verification"]
                                                              :method        :post
                                                              :response-code 201}}
                :create-password-change-ticket     {:name    :create-password-change-ticket
                                                    :doc     "Create a password change ticket for a user."
                                                    :doc-url "https://auth0.com/docs/api/management/v2#!/Tickets/post_password_change"
                                                    :http    {:path          ["tickets" "password-change"]
                                                              :method        :post
                                                              :response-code 201}}}})
