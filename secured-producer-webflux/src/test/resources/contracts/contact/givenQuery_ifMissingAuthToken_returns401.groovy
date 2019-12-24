package contracts.contact

import org.springframework.cloud.contract.spec.Contract

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 2019-06-23 22:58
 */
Contract.make {
    description("contact search when user not authorized responds 401")

    request {
        method(GET())
        urlPath('/contact/search') {
            queryParameters {
                parameter 'query': equalTo('hel')
            }
        }
    }
    response {
        status(UNAUTHORIZED())
    }
}