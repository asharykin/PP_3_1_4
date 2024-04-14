function getCurrentUser() {
    fetch('/user_rest')
        .then(response => response.json())
        .then(user => {
            let roles = user.roles.map(role => role.role.replace('ROLE_', '')).sort().join(' ');
            let email = document.getElementById('emailCurrentUser');
            email.textContent = user.email;
            let role = document.getElementById('roleCurrentUser');
            role.textContent = roles;
        });
}

getCurrentUser()

function addUser() {
    let form = document.getElementById('formCreate');
    form.addEventListener('submit', event => {
        event.preventDefault();
        let userRoles = [];
        for (let i = 0; i < form.roles.options.length; i++) {
            if (form.roles.options[i].selected) {
                userRoles.push(form.roles.options[i].text);
            }
        }
        let userData = {
            firstName: form.firstname.value,
            lastName: form.lastname.value,
            age: form.age.value,
            email: form.email.value,
            password: form.password.value,
            roles: userRoles
        }
        fetch('/admin_rest', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        }).then(() => {
            form.reset();
            document.getElementById('home-tab').click();
            getUsersTable();
        });
    });
}

function updateUser(id) {
    let form = document.getElementById('formEdit');
    let modal = document.getElementById('modalEdit');
    modal.style.display = 'block';
    fetch("/admin_rest/" + id)
        .then(response => response.json())
        .then(user => {
            form.id.value = user.id;
            form.firstname.value = user.firstName;
            form.lastname.value = user.lastName;
            form.age.value = user.age;
            form.password.value = '';
            form.email.value = user.email;
        });
    form.addEventListener('submit', event => {
        event.preventDefault();
        let userRoles = [];
        for (let i = 0; i < form.roles.options.length; i++) {
            if (form.roles.options[i].selected) {
                userRoles.push(form.roles.options[i].text);
            }
        }
        let userData = {
            id: form.id.value,
            firstName: form.firstname.value,
            lastName: form.lastname.value,
            age: form.age.value,
            email: form.email.value,
            password: form.password.value,
            roles: userRoles
        }
        fetch('/admin_rest', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        }).then(() => {
            document.getElementById('closeEdit').click();
            getUsersTable();
        });
    });
}

function deleteUser(id) {
    let form = document.getElementById('formDelete');
    let modal = document.getElementById('modalDelete');
    modal.style.display = 'block';
    fetch("/admin_rest/" + id)
        .then(response => response.json())
        .then(user => {
            form.id.value = user.id;
            form.firstname.value = user.firstName;
            form.lastname.value = user.lastName;
            form.age.value = user.age;
            form.email.value = user.email;
        })
    form.addEventListener('submit', event => {
        event.preventDefault();
        fetch("/admin_rest/" + id, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(() => {
            document.getElementById('closeDelete').click();
            getUsersTable();
        });
    });
}

function getUsersTable() {
    let table = document.getElementById('allUsersTable');
    table.innerHTML = '';
    fetch("/admin_rest")
        .then(response => response.json())
        .then(users => {
            console.log(users);
            users.forEach(user => {
                let roles = user.roles.map(role => role.role.replace('ROLE_', '')).sort().join(' ');
                let row = document.createElement('tr');
                row.innerHTML = `
                    <td class="pt-3">${user.id}</td>
                    <td class="pt-3">${user.firstName}</td>
                    <td class="pt-3">${user.lastName}</td>
                    <td class="pt-3">${user.age}</td>
                    <td class="pt-3">${user.email}</td>
                    <td class="pt-3">${roles}</td>
                    <td>
                        <button type="button" class="btn btn-info" data-toggle="modal" data-target="#modalEdit" onclick="updateUser(${user.id})">Edit</button>
                    </td>
                    <td>
                        <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#modalDelete" onclick="deleteUser(${user.id})">Delete</button>
                    </td>
                `;
                table.appendChild(row);
            });
        })
}

getUsersTable();
