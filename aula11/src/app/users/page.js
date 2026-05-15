import ListUsers from './_components/list-user'

export default async function UserListPage() {
    const res = await fetch('https://jsonplaceholder.typicode.com/users');
    const users = await res.json();
    
    return (
        <>
            <a href='/users/new/'>Novo Usuario</a>
            <ListUsers data={users} />
        </>
    )
}