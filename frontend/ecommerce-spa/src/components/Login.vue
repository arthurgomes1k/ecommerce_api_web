<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-md-6">
        <h2 class="text-center">Login</h2>
        <form @submit.prevent="login">
          <div class="form-group mt-3">
            <label for="username">Username</label>
            <input type="text" class="form-control" id="username" v-model="username" required>
          </div>
          <div class="form-group mt-3">
            <label for="password">Senha</label>
            <input type="password" class="form-control" id="password" v-model="password" required>
          </div>
          <button type="submit" class="btn btn-primary mt-3">Entrar</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import router from '../router'

export default {
  data () {
    return {
      username: '',
      password: ''
    }
  },
  methods: {
    async login () {
      const response = await fetch('http://localhost:8080/api/users/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          username: this.username,
          password: this.password
        })
      })
      try {
        const json = await response.json()
        if (json && json.token) {
          localStorage.setItem('token', `Bearer ${json.token}`)
          router.push('/products')
          window.location.reload()
        } else {
          console.error('Invalid response from server')
        }
      } catch (error) {
        alert('Erro, nome do usuário ou senha inválidos.')
      }
    }
  }
}
</script>
