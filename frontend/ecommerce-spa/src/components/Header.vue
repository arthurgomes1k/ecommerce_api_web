<template>
  <header>
    <div class="logo">
      <img src="../assets/logo-product.svg" alt="Logo">
    </div>

    <nav>
      <ul>
        <li><router-link to="/products">Products</router-link></li>
        <li><router-link to="/categories">Categories</router-link></li>
        <li v-if="isAuthenticated && isAdmin"><router-link to="/users">Users</router-link></li>
      </ul>
    </nav>

    <div class="user-session" v-if="isAuthenticated">
      <img :src="baseUrl + user.image" alt="User Image" @click="toggleDropdown">
      <div class="dropdown" v-show="showDropdown">
        <ul>
          <li><router-link :to="'/users/' + user.id">Profile</router-link></li>
          <li><a href="#" @click="logout">Logout</a></li>
        </ul>
      </div>
    </div>
  </header>
</template>

<script>
import axios from 'axios'

export default {
  data () {
    return {
      baseUrl: 'http://localhost:8080',
      user: {},
      showDropdown: false
    }
  },
  computed: {
    isAuthenticated () {
      const token = localStorage.getItem('token')
      return !!token
    },
    isAdmin () {
      const userCopy = JSON.parse(JSON.stringify(this.user))
      return userCopy.roles && userCopy.roles.some(role => role.name === 'ROLE_ADMIN')
    }
  },
  mounted () {
    const token = localStorage.getItem('token')
    if (token) {
      this.fetchUserDetails()
    }
  },
  methods: {
    fetchUserDetails () {
      const token = localStorage.getItem('token')
      if (token) {
        axios.defaults.headers.common['Authorization'] = token
        axios
          .get('http://localhost:8080/api/users/me')
          .then(response => {
            this.user = response.data
          })
          .catch(error => {
            console.log(error)
          })
      }
    },
    toggleDropdown () {
      this.showDropdown = !this.showDropdown
    },
    logout () {
      localStorage.removeItem('token')
      this.user = {} // Limpa os detalhes do usuário ao fazer logout
      this.$router.push('/')
    }
  }
}
</script>

<style scoped>
/* Estilos CSS específicos para o componente de cabeçalho */
/* Adicione os estilos conforme necessário para personalização */
header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
}

.logo img {
  height: 50px;
}

nav ul {
  list-style: none;
  display: flex;
  gap: 10px;
}

.user-session {
  display: flex;
  align-items: center;
}

.user-session img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  cursor: pointer;
}

.dropdown {
  position: absolute;
  top: 50px;
  right: 10px;
  background-color: #fff;
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 10px;
  display: none;
}

.dropdown ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.dropdown ul li {
  margin-bottom: 5px;
}

.dropdown ul li a {
  text-decoration: none;
  color: #333;
  cursor: pointer;
}

.dropdown ul li a:hover {
  text-decoration: underline;
}
</style>
