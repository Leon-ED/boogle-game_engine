FROM node:19-alpine as builder
WORKDIR /app
COPY . .
ENV REACT_APP_BACKEND_URL myprotocol//myhost/chat
RUN npm ci
RUN npm run build


