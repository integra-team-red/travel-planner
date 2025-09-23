<script setup lang="ts">
    import {ref} from 'vue'

    // const user = ref('Mr. guest');
    const prName = ref('');
    const prType = ref('');
    // const logText = ref('Log in');

    function submitProposal() {
        // do what you gotta do with the data
        console.log(prName)
        console.log(prType)
        prName.value = '';
        prType.value = '';
    }
</script>

<template>
    <body class="mx-100">
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
