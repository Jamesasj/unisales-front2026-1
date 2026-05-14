'use client'
import FormInput from '@/components/formInput'
import { useState } from 'react'
export default function NewUserPage() {
    const [user, setUser] = useState({name:'', email:'', password:''});

    const changeName = (newValue)=>{
        setUser({...user, name:newValue}) //'...' destructor 
    }
    const changeEmail = (newValue)=>{
        setUser({...user, email:newValue}) //'...' destructor 
    }
        const changePass = (newValue)=>{
        setUser({...user, password:newValue}) //'...' destructor 
    }

    return (
        <>
            <h1>Novo Usuario - {user.name}</h1>

            <div>
                <FormInput name='Nome' value={user.name} onChange={changeName}  />
                <FormInput name='e-mail' value={user.email} onChange={changeEmail}  />
                <FormInput name='Senha' value={user.password} onChange={changePass} type='password'  />
            </div>
        </>
    )
}