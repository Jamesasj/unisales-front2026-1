import ListUsers from '@/components/list-user'

export default function UserListPage() {
    return (
        <>
            <a href='/users/new/'>Novo Usuario</a>
            <ListUsers />
        </>
    )
}