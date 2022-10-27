import {HTTP} from "@/helpers/http.helper";
import { endpoints } from "@/helpers/endpoint.helper";

export const subscriptionService = {
    get_subscription,
    cancel_subscription
}

function get_subscription() {
    return HTTP.get(endpoints.subscription_endpoint());
}

function cancel_subscription() {
    return HTTP.post(endpoints.subscription_endpoint() + '/cancel');
}
