<template>
  <div>
    <h2 class="mt-2">Product Registation</h2>
    <form @submit.prevent="save" class="m-5">
      <div class="form-group" v-if="product.image">
        <img :src="baseUrl + product.image" class="rounded mx-auto d-block" :alt="`${product.name}`">
      </div>
      <div class="form-group">
        <label for="formName" class="form-label">Product name</label>
        <input id="formName" type="text" v-model="product.name" class="form-control" placeholder="Product name">
      </div>
      <div class="form-group">
        <label for="formDescription" class="form-label">Product description</label>
        <input id="formDescription" type="text" v-model="product.description" class="form-control"
          placeholder="Product description">
      </div>
      <div class="form-group">
        <label for="formPrice" class="form-label">Product price</label>
        <input id="formPrice" type="number" step=".01" v-model="product.price" class="form-control" placeholder="0.0">
      </div>
      <div class="form-group">
        <label for="formCategory" class="form-label">Product category</label>
        <select id="formCategory" v-model="selectedCategory" class="form-select">
          <option v-for="category in categories" :key="category.id" :value="category.id">{{ category.name }}</option>
        </select>
      </div>
      <div class="form-group">
        <label for="formImage" class="form-label">Image</label>
        <input class="form-control" type="file" id="formImage" name="image">
      </div>
      <div class="mt-3 mb-3">
        <button type="submit" class="btn btn-primary">Save</button>
      </div>
    </form>
  </div>
</template>

<script>
import Vue from 'vue'
import axios from 'axios'
import router from '../router'

export default {
  name: 'Product',
  data () {
    return {
      baseUrl: 'http://localhost:8080',

      result: [],

      categories: [],
      selectedCategory: null,

      product: {
        id: '',
        name: '',
        description: '',
        price: '',
        category: null,
        image: null
      }
    }
  },
  created () {
    axios.interceptors.request.use(
      config => {
        const token = localStorage.getItem('token')
        if (token) {
          config.headers.Authorization = token
        }
        return config
      },
      error => {
        return Promise.reject(error)
      }
    )
    this.getProductList()
  },
  mounted () {
    // Extrai o id do parâmetro da URL usando a biblioteca `vue-router`
    const productId = this.$route.params.id
    if (productId) {
      this.getProduct(productId)
    }
    this.getCategoryList()
  },
  methods: {
    cloneProduct (product) {
      return JSON.parse(JSON.stringify(product))
    },
    getProductList () {
      axios.get('http://localhost:8080/api/products')
        .then(({ data }) => {
          this.result = data
          this.originalList = data.map(p => ({ ...p }))
        })
        .catch((error) => {
          alert('Error loading products!')
          console.log(error)
        })
    },
    async getCategoryList () {
      console.log(localStorage.getItem('token'))
      axios.get('http://localhost:8080/api/categories')
        .then(({ data }) => {
          this.result = data
          this.categories = data.map(p => ({ ...p }))
        })
        .catch((error) => {
          alert('Error loading categories!')
          console.log(error)
        })
    },
    save () {
      if (this.product.id) {
        this.updateProduct()
      } else {
        this.saveProduct()
      }
    },
    saveProduct () {
      const formData = new FormData()
      formData.append('name', this.product.name)
      formData.append('description', this.product.description)
      formData.append('price', parseFloat(this.product.price))
      formData.append('category', this.selectedCategory)

      // adicionar a imagem selecionada ao FormData
      const imageInput = document.querySelector('#formImage')
      if (imageInput.files[0]) {
        formData.append('image', imageInput.files[0])
      }

      axios
        .post('http://localhost:8080/api/products', formData)
        .then(({ data }) => {
          alert('Product saved successfully!')
          router.push('/products')
        })
        .catch((error) => {
          alert('Error saving product!')
          console.log(error)
        })
    },
    updateProduct () {
      const oldProduct = this.originalList.find(p => p.id === this.product.id)
      const formData = new FormData()
      const updatedFields = {}

      if (this.product.name !== oldProduct.name) {
        formData.append('name', this.product.name)
        updatedFields.name = this.product.name
      }

      if (this.product.description !== oldProduct.description) {
        formData.append('description', this.product.description)
        updatedFields.description = this.product.description
      }

      if (this.product.price !== oldProduct.price) {
        formData.append('price', this.product.price)
        updatedFields.price = parseFloat(this.product.price)
      }

      if (this.selectedCategory !== oldProduct.category.id) {
        formData.append('category', this.selectedCategory)
        updatedFields.category = this.selectedCategory
      }

      // adicionar a imagem selecionada ao FormData
      const imageInput = document.querySelector('#formImage')
      const imageFile = imageInput.files[0]
      if (imageFile) {
        formData.append('image', imageFile)
      }

      const index = this.result.findIndex((p) => p.id === this.product.id)
      if (index !== -1) {
        const updatedProduct = { ...this.result[index] }
        updatedProduct.name = updatedFields.name || updatedProduct.name
        updatedProduct.description = updatedFields.description || updatedProduct.description
        updatedProduct.price = updatedFields.price || updatedProduct.price
        updatedProduct.category = this.categories.find(category => category.id === updatedFields.category) || updatedProduct.category
        this.result.splice(index, 1, updatedProduct)
      }

      const headers = {
        'Content-Type': 'multipart/form-data', // Defina o tipo de conteúdo como 'multipart/form-data' para o FormData
        'Authorization': localStorage.getItem('token') // Adicione o cabeçalho de autorização com o token do localStorage
      }

      axios
        .patch(`http://localhost:8080/api/products/${oldProduct.id}`, formData, { headers })
        .then(({ data }) => {
          alert('Product updated successfully!')
          // atualize a variável 'result' com os dados atualizados
          const index = this.result.findIndex((p) => p.id === this.product.id)
          if (index !== -1) {
            const updatedProduct = { ...this.result[index], ...updatedFields }
            Vue.set(this.result, index, updatedProduct)
          }

          router.push('/products')
        })
        .catch((error) => {
          alert('Error updating product!')
          console.log(error)
        })
    },
    editProduct (product) {
      this.product = this.cloneProduct(product)
      this.selectedCategory = this.product.category.id
    },
    getProduct (productId) {
      // Busca o produto pelo id usando a API
      axios.get(`http://localhost:8080/api/products/${productId}`)
        .then(response => {
          const data = response.data
          // Preenche o objeto `product` com os dados do produto retornado
          this.product = {
            id: data.id,
            name: data.name,
            description: data.description,
            price: data.price,
            category: data.category,
            image: data.image
          }
          // Define a categoria selecionada no dropdown
          this.selectedCategory = data.category.id
        })
        .catch(error => {
          console.log(error)
        })
    },
    resetForm () {
      this.product = {
        id: '',
        name: '',
        description: '',
        price: '',
        category: null
      }
      this.selectedCategory = null
    },
    submitForm () {
      if (this.product.id) {
        this.updateProduct()
      } else {
        this.saveProduct()
      }
    }
  }
}

</script>
