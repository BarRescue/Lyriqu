import {HTTP} from "@/helpers/http.helper";
import { endpoints } from "@/helpers/endpoint.helper";

export const musicService = {
    get_categories,
    get_category,
    create_category,
    get_songs_by_user,
    publish_song
}

function get_categories() {
    return HTTP.get(endpoints.music_endpoint() + '/category');
}

function get_category(id) {
    return HTTP.get(endpoints.music_endpoint() + '/category/' + id);
}

function create_category(category) {
    let formData = new FormData();

    formData.append("file", category.file);
    formData.append("name", category.name);
    formData.append("description", category.description);

    return HTTP.post(endpoints.music_endpoint() + '/category', formData, {
        headers: {
            "Content-Type": "multipart/form-data"
        }
    })
}

function get_songs_by_user() {
    return HTTP.get(endpoints.music_endpoint() + '/song');
}

function publish_song(song) {
    let formData = new FormData();

    formData.append("file", song.file);
    formData.append("name", song.name);
    formData.append("categories", song.categories);
    formData.append("image", song.image);

    return HTTP.post(endpoints.music_endpoint() + '/song', formData, {
        headers: {
            "Content-Type": "multipart/form-data"
        }
    })
}
