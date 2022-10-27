import axios from 'axios';
import {store} from '@/store';

const HTTP = axios.create({
  headers: {
      Authorization: 'Bearer ' + store.getters["user/getToken"]
  }
})

HTTP.interceptors.request.use(
function(config) {
    const jwt = store.getters["user/getToken"]
      config.headers.Authorization = `Bearer ${jwt}`;
    return config;
})

export {HTTP};
