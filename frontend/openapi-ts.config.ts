import { defineConfig } from '@hey-api/openapi-ts';

export default defineConfig({
    input: 'http://localhost:8080/api/v3/api-docs',
    output: 'src/client',
    plugins: [
        '@tanstack/vue-query',
    ]
});
