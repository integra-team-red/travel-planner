import {computed, ref} from "vue";
import {defineStore} from "pinia";
import {type UserDTO, UserRoleEnum} from '../../typescript-client';


export const useUserStore = defineStore('user', () => {
    const user = ref<UserDTO | null>(null)

    function set(newUser: UserDTO | null): void {
        user.value = newUser
    }

    const isLoggedIn = computed(() => {
        return user.value !== null
    })

    const isAdmin = computed(() => user.value?.roles?.includes(UserRoleEnum.Admin))

    return {user, set, isLoggedIn, isAdmin}
})
