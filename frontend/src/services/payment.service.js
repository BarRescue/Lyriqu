import {HTTP} from "@/helpers/http.helper";
import { endpoints } from "@/helpers/endpoint.helper";

export const paymentService = {
    create_mandate,
    create_customer,
    get_payments
}

function create_customer() {
    return HTTP.post(endpoints.payment_endpoint() + '/create/customer');
}

function create_mandate() {
    return HTTP.post(endpoints.payment_endpoint() + '/create/mandate');
}

function get_payments() {
    return HTTP.get(endpoints.payment_endpoint());
}
