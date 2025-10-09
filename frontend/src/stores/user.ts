import {computed, ref} from "vue";
import {defineStore} from "pinia";


type User = {
    email: string
    roles: string[]
}

export const useUserStore = defineStore('user', () => {
    const user = ref<User | null>(null)

    function set(newUser: User): void {
        user.value = newUser
    }

    const isLoggedIn = computed(() => {
        return user.value !== null
    })


    return {user, set, isLoggedIn}
})