<script setup lang="ts">
    import { ref } from 'vue'
    const user = ref('Mr. user');
    const prName = ref('');
    const prType = ref('');
    function submitProposal () {
        // do what you gotta do with the data
        console.log(prName)
        console.log(prType)
        prName.value = '';
        prType.value = '';
    }
</script>

<template>
    <body  class="m-100">
    <h1>Hello, {{user}}!</h1>
    <div>
        <RouterLink to="cities">
            <Button as="a" label="Cities"/>
        </RouterLink>
        <RouterLink to="restaurants">
            <Button as="a" label="Restaurants"/>
        </RouterLink>
    </div>
    <br>
    <h3>Submit a proposal (does nothing):</h3>
    <div class="w-100">
        <InputGroup>
            <InputGroupAddon>
                <i class="pi pi-user"></i>
            </InputGroupAddon>
            <InputText v-model="prName" placeholder="Point of interest's name"/>
        </InputGroup>
        <InputGroup>
            <InputGroupAddon>
                <i class="pi pi-user"></i>
            </InputGroupAddon>
            <InputText v-model="prType" placeholder="Type"/>
        </InputGroup>
        <Button label="Submit" @click="submitProposal"></Button>
    </div>
    </body>
</template>

<style scoped>

</style>
