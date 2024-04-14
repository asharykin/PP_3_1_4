function getCurrentUser() {
    fetch("/user_rest")
        .then(response => response.json())
        .then(user => {
            let roles = user.roles.map(role => role.role.replace('ROLE_', '')).sort().join(' ');
            let email = document.getElementById('emailCurrentUser');
            email.textContent = user.email;
            let role = document.getElementById('roleCurrentUser');
            role.textContent = roles;
            let table = document.getElementById('userTable');
            let row = document.createElement('tr');
            row.innerHTML = `
                <td>${user.id}</td>
                <td>${user.firstName}</td>
                <td>${user.lastName}</td>
                <td>${user.age}</td>
                <td>${user.email}</td>
                <td>${roles}</td>
            `;
            table.appendChild(row);
        });
}

getCurrentUser()