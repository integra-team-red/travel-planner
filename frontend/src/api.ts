import {
    AuthControllerApi,
    CityControllerApi,
    Configuration,
    PoiControllerApi,
    ProposalControllerApi,
    RestaurantControllerApi,
} from '../typescript-client';

function getToken() {
    return localStorage.getItem('jwt') || '';
}

const config = new Configuration({
    accessToken: getToken,
    basePath: '/api'
});

const authApi = new AuthControllerApi(config);
const proposalApi = new ProposalControllerApi(config);
const poiApi = new PoiControllerApi(config);
const cityApi = new CityControllerApi(config);
const restaurantApi = new RestaurantControllerApi(config);

export {authApi, proposalApi, poiApi, cityApi, restaurantApi};
