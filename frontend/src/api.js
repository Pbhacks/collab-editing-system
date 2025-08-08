// API wrapper using Axios. All calls go through API Gateway.
import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api", // API Gateway
  headers: {
    "Content-Type": "application/json"
  },
  timeout: 10000
});

// simple response interceptor (could add auth here)
api.interceptors.response.use(
  res => res,
  err => {
    // keep simple for now
    return Promise.reject(err);
  }
);

export default api;
