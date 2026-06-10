'use client'

import { useEffect, useState } from 'react';
import FormInput from '@/components/FormInput';
import Button from '@/components/Button';
import api from "@/utils/api"
import { useParams } from 'next/navigation';

export default function UserEditPage() {
    const [user, setUser] = useState({ username: '', email: '', password: '', profile: '' });
    const params = useParams();

    useEffect(()=>{
        console.log(params)
        const loadUser = async() =>{
            const tempUser = await api.get(`/users/${params.id}`);
            setUser({...tempUser})
        }
        loadUser()
    },[])
    const handleChange = (e) => {
        const { name, value } = e.target;
        setUser((prevUser) => ({ ...prevUser, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        api.put('/users', user)
    };

    return (
        <>
            <h1>Editar Usuario</h1>
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
                <Button type="submit">Salvar Usuario</Button>
            </div>
        </>
    );
}