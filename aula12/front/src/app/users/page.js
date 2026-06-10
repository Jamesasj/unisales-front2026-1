'use client'
import api from '@/utils/api';
import style from './style.css';
import { useEffect, useState } from 'react';
import Button from '@/components/Button';

export default function UserPage() {
    const [lista, setLista] = useState([])

    useEffect(() => {
        const exec = async () => {
            const res = await api.get('/users');
            setLista(res)
        }
        exec();
    }, [])



    return (
        <>
            <h1>User</h1>
            <div>
                <Button onClick={()=>{location.href='users/new'}}>Novo</Button>
            </div>
            <table style={style.table}>
                <tr>
                    <th>username</th>
                    <th>email</th>
                    <th>profile</th>
                </tr>
                {
                    lista.map((user) => {
                        return (
                            <tr key={user.id}>
                                <td>{user.username}</td>
                                <td>{user.email}</td>
                                <td>{user.profile}</td>
                                <td><Button onClick={()=> location.href = `users/${user.id}`}>Editar</Button></td>
                            </tr>
                        )
                    })
                }
            </table>
        </>
    )
}