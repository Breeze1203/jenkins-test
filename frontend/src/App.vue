<template>
  <div id="app" class="container">
    <h1 class="title">Frontend App</h1>
    <p class="subtitle">Click the button below to get a message from the backend service.</p>
    <button @click="fetchMessage" class="action-button">
      Get Message from Backend
    </button>
    <p v-if="message" class="message-display">
      <strong>{{ message }}</strong>
    </p>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'App',
  data() {
    return {
      message: ''
    };
  },
  methods: {
    fetchMessage() {
      // Show a loading message immediately
      this.message = 'Loading...';
      axios.get('/api/hello')
          .then(response => {
            this.message = response.data.message;
          })
          .catch(error => {
            console.error("There was an error!", error);
            this.message = 'Failed to load message from backend.';
          });
    }
  }
}
</script>

<style>
/* Import a nice font from Google Fonts (optional but recommended) */
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;700&display=swap');

/* Basic styles for the whole page */
body {
  background-color: #f4f7f6;
  font-family: 'Inter', sans-serif;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  margin: 0;
}

/* Main container for our app component */
.container {
  background-color: #ffffff;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.05);
  text-align: center;
  max-width: 500px;
  width: 90%;
  transition: transform 0.3s ease;
}

.container:hover {
  transform: translateY(-5px);
}

/* Main title style */
.title {
  font-size: 2.25rem; /* 36px */
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 10px;
}

/* Subtitle style */
.subtitle {
  font-size: 1rem; /* 16px */
  color: #7f8c8d;
  margin-bottom: 30px;
}

/* The main button */
.action-button {
  background-color: #3498db; /* A nice blue */
  color: white;
  border: none;
  padding: 15px 30px;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
  box-shadow: 0 4px 15px rgba(52, 152, 219, 0.2);
}

/* Button hover effect */
.action-button:hover {
  background-color: #2980b9; /* A darker blue */
  transform: translateY(-2px);
}

/* Button active (click) effect */
.action-button:active {
  transform: translateY(0);
}

/* The area where the message from the backend is displayed */
.message-display {
  margin-top: 30px;
  padding: 20px;
  background-color: #ecf0f1;
  border-radius: 8px;
  color: #34495e;
  font-size: 1.1rem;
  font-weight: 500;
  border-left: 5px solid #3498db;
  text-align: left;
}
</style>
