# How to view OpenAPI spec
There are two ways to view this app's specs:

1. **(Complete)** Navigate to either of these links after booting the app: 
- _**{server}**_:_**{server_port}**_/_**{context_path}**_/swagger-ui.html
- _**{server}**_:_**{server_port}**_/_**{context_path}**_/swagger-ui/index.html

    Where:
  - **{server}** - localhost
  - **{server_port}** - 8080
  - **{context_path}** - api

After landing on one of these pages, click the blue hyperlink below the "OpenAPI definition" heading 
to view the specification as a JSON file.
2. **(Partial; lacks headers)** From IntelliJ:
- go to the 'Main Menu' (Alt + \\)
- select 'View' -> 'Tool Windows'
- select 'Endpoints'
- select all listed endpoints (click the first entry; scroll down; shift click last entry)
- go to the 'OpenAPI' tab and click on the floppy disk icon

Once the preview's done loading, you may go to 'Editor' (check top right icons),
copy all text (Ctrl + A) and save it permanently to a YAML file if you wish.

# How to work with openapi-ts (generating typescript REST clients)
## REST Client Generation
To generate a fresh REST client, **you'll first need to have the backend running (default)**.
To change this behaviour and instead use a file directly, refer to the below point on
OpenAPI spec generation.

After starting the backend server, run `npm run openapi-ts` from a CLI in travel-planner\frontend.
The generation output can be found in frontend\src\client by default.
## Configuring the Generation Behaviour
To configure settings like the input file / URL (must be JSON / YAML) and output folder you'll have to modify openapi-ts.config.ts

# frontend

This template should help get you started developing with Vue 3 in Vite.

## Recommended IDE Setup

[VSCode](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) (and disable Vetur).

## Type Support for `.vue` Imports in TS

TypeScript cannot handle type information for `.vue` imports by default, so we replace the `tsc` CLI with `vue-tsc` for type checking. In editors, we need [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) to make the TypeScript language service aware of `.vue` types.

## Customize configuration

See [Vite Configuration Reference](https://vite.dev/config/).

## Project Setup

```sh
npm install
```

### Compile and Hot-Reload for Development

```sh
npm run dev
```

### Type-Check, Compile and Minify for Production

```sh
npm run build
```

### Lint with [ESLint](https://eslint.org/)

```sh
npm run lint
```
