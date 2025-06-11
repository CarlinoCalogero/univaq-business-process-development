export type ApiError = {
    status: string,
    statusCode: number,
    message: string,
    path: string,
    requestMethod: string,
    timestamp: string
}