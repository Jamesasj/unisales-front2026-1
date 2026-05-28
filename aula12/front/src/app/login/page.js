'use client';
import { useState } from "react";
import Button from "@/components/Button";
import FormInput from "@/components/FormInput";

export default function LoginPage() {
  const [user, setUser] = useState({ email: "", password: "" });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setUser((prevUser) => ({ ...prevUser, [name]: value }));
  };

  const authenticate = () => {
    fetch("http://localhost:8080/api/auth/login", {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        "email": user.email,
        "password": user.password
      })
    }).then((res) => {
      if(res.ok){
        return res.json();
      }
    }).then((obj)=>{
      location.href='/home'
      localStorage.setItem('API-KEY', obj.data.accessToken )
      console.log(obj)
    })
  };

  const loadBootstrap = () => {
    fetch("http://localhost:8080/api/public/bootstrap", {
      method: "POST",
    })
      .then((response) => response.json())
      .then((data) => console.log("Bootstrap carregado:", data))
      .catch((error) => console.error("Erro ao carregar Bootstrap:", error));
  };

  return (
    <div>
      <h1>Login</h1>
      <FormInput label="Email" type="email" name="email" value={user.email} onChange={handleChange} />
      <FormInput label="Password" type="password" name="password" value={user.password} onChange={handleChange} />
      <Button type="submit" onClick={authenticate}>Login</Button>
      <Button type="button" onClick={() => console.log("Redirecionar para cadastro")}>Cadastrar</Button>
      <Button type="button" onClick={loadBootstrap}>Carregar Bootstrap</Button>
    </div>
  );
}
