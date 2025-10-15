import {computed, ref} from "vue";
import {defineStore} from "pinia";
import type {UserDTO} from '../../typescript-client';


export const useUserStore = defineStore('user', () => {
    const user = ref<UserDTO | null>(null)

    function set(newUser: UserDTO | null): void {
        user.value = newUser
    }

    const isLoggedIn = computed(() => {
        return user.value !== null
    })


    return {user, set, isLoggedIn}
})
