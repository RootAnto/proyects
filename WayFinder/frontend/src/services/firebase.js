import { initializeApp } from 'firebase/app';
import { getAuth, GoogleAuthProvider } from 'firebase/auth';

const firebaseConfig = {
  apiKey: "AIzaSyD7rOMQQXmYslLH7e4fnaGr40fkv_UEaoY",
  authDomain: "wayfinder-6a444.firebaseapp.com",
  projectId: "wayfinder-6a444",
  storageBucket: "wayfinder-6a444.appspot.com",
  messagingSenderId: "1026625569982",
  appId: "1:1026625569982:web:e2c38181ba3d9a7720caef"
};

const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const provider = new GoogleAuthProvider();

export { auth, provider };