import axios from "axios";

export const apiClient = axios.create({
  baseURL: "http://localhost:8080/api",
});

export interface Problem {
  id: number;
  title: string;
  description: string;
  boilerplate: string;
}

export interface ExecutionResult {
  stdout: string;
  stderr: string;
  exitCode: number;
  timedOut: boolean;
  execTimeMs: number;
}
