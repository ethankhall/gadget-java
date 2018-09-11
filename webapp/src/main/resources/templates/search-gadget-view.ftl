<!DOCTYPE html>
<#import "macros.ftl" as m>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
          integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
</head>
<body class="text-center">

<div class="cover-container d-flex w-100 h-100 p-3 mx-auto flex-column">
    <@m.header />
    <main role="main">
        <!-- Main jumbotron for a primary marketing message or call to action -->
        <div class="jumbotron">
            <div class="container">
                <h1 class="display-3">Searching for ${searchString}...</h1>
                <form action="/gadget/new">
                    <input type="hidden" name="name" value="${searchString}">
                    <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Add/update redirect for ${searchString}</button>
                </form>
                <table class="table">
                    <thead>
                    <tr>
                        <th scope="col">Alias</th>
                        <th scope="col">Destination</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list result as item>
                        <tr>
                            <td>${item.source}</td>
                            <td><a href="${item.destination}">${item.destination}</a></td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
                <p>Total number of matched records: ${totalCount}</p>
            </div>
        </div>

    </main>

    <@m.footer />
</body>
</html>