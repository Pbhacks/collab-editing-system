// src/api.js
import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api", // API Gateway
  headers: {
    "Content-Type": "application/json"
  },
  timeout: 15000
});

api.interceptors.response.use(
  res => res,
  err => {
    // pass through error to UI
    return Promise.reject(err);
  }
);

export default api;
