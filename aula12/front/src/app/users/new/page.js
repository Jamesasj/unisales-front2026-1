'use client'
import FormInput from "@/components/FormInput";
import api from "@/utils/api";
import { useState } from "react";

export default function UserNewPage() {
    const [user, setUser] = useState({ username: "", email: "", password: "", profile: "" })

    const handleChange = (e) => {
        const { name, value } = e.target;
        setUser((prevUser) => ({ ...prevUser, [name]: value }));
    }
    const handleSubmit = (e) => {
        api.post('/users', user)
    }
    return (<>
        <h1>Novo Usuario</h1>
        <div>
            <FormInput label="Username" type="text" name="username" value={user.username} onChange={handleChange}></FormInput>
            <FormInput label="Email" type="email" name="email" value={user.email} onChange={handleChange}></FormInput>
            <FormInput label="Password" type="password" name="password" value={user.password} onChange={handleChange}></FormInput>
            <select name="profile" value={user.profile} onChange={handleChange}>
                <option value="">Selecione o perfil</option>
                <option value="ADMIN">ADMIN</option>
                <option value="COORDENADOR">COORDENADOR</option>
                <option value="PROFESSOR">PROFESSOR</option>
                <option value="ALUNO">ALUNO</option>
                <option value="AVALIADOR_EXTERNO">AVALIADOR_EXTERNO</option>
            </select>
            <button onClick={handleSubmit}>Criar Usuário</button>
        </div>
    </>)
}