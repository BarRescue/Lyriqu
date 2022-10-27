import {HTTP} from "@/helpers/http.helper";
import { endpoints } from "@/helpers/endpoint.helper";

export const monitorService = {
    get_entries,
    get_entry,
    approve_entry,
    deny_entry
}

function get_entries() {
    return HTTP.get(endpoints.monitor_endpoint());
}

function get_entry(id) {
    return HTTP.get(endpoints.monitor_endpoint() + '/' + id);
}

function approve_entry(id) {
    return HTTP.post(endpoints.monitor_endpoint() + '/' + id + '/approve');
}

function deny_entry(id, reason) {
    return HTTP.post(endpoints.monitor_endpoint() + '/' + id + '/deny', {
        reason
    });
}
