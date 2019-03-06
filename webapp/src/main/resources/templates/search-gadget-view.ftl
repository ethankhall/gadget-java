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
    <style>
        .divider {
            width: 5px;
            height: auto;
            display: inline-block;
        }
    </style>
</head>
<body class="text-center">

<div class="cover-container d-flex w-100 h-100 p-3 mx-auto flex-column">
    <@m.header />
    <main role="main">
        <!-- Main jumbotron for a primary marketing message or call to action -->
        <div class="jumbotron">
            <div class="container">
                <div class="row align-items-center">
                    <div class="col">
                        <h1 class="display-3">Searching for `${name}`</h1>
                    </div>
                </div>

                <div class="row align-items-center">
                    <div class="col">
                        <button class="btn btn-outline-primary my-2 my-sm-0" type="button"
                                onclick="window.location.href = '/gadget/new?name=${name}'">Create a new link
                        </button>

                        <div class="divider"></div>

                        <button class="btn btn-outline-secondary my-2 my-sm-0" type="button"
                                onclick="window.location.href = '/gadget/search'">See All
                        </button>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <ul class="pagination">
                            <#if previousPages?size == 0>
                                <li class="page-item disabled"><a class="page-link" href="#" tabindex="-1">Previous</a></li>
                            <#else>
                                <li class="page-item"><a class="page-link" href="${previousPages?last.url}" tabindex="-1">Previous</a></li>
                            </#if>
                            <#list previousPages as item>
                                <li class="page-item"><a class="page-link" href="${item.url}">${item.getPageNumber()}</a></li>
                            </#list>
                            <#list currentPage as item>
                                <li class="page-item active"><a class="page-link" href="${item.url}">${item.getPageNumber()}</a></li>
                            </#list>
                            <#list nextPages as item>
                                <li class="page-item"><a class="page-link" href="${item.url}">${item.getPageNumber()}</a></li>
                            </#list>
                            <#if nextPages?size == 0>
                                <li class="page-item disabled"><a class="page-link" href="#" tabindex="-1">Next</a></li>
                            <#else>
                                <li class="page-item"><a class="page-link" href="${nextPages?first.url}">Next</a></li>
                            </#if>
                        </ul>
                    </div>
                </div>

                <div class="row align-items-center">
                    <div class="col">
                        <table class="table">
                            <thead>
                            <tr>
                                <th scope="col">Alias</th>
                                <th scope="col">Destination</th>
                                <th scope="col">Created By</th>
                                <th scope="col">Edit</th>
                            </tr>
                            </thead>
                            <tbody>
                            <#list result as item>
                                <tr>
                                <td>${item.source}</td>
                                <td><a href="${item.destination}">${item.destination}</a></td>
                                <td>${item.getUserName()}</td>
                                <td>
                            <a href="/gadget/edit?name=${item.source}">
                                    <button type="button" class="btn btn-warning">Edit</button></a>
                            <a href="/gadget/delete?name=${item.source}">
                                <button type="button" class="btn btn-danger">Delete</button></a>
                                </td>
                                </tr>
                            </#list>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div class="row">
                    <div class="col">
                        <ul class="pagination">
                            <#if previousPages?size == 0>
                                <li class="page-item disabled"><a class="page-link" href="#" tabindex="-1">Previous</a></li>
                            <#else>
                                <li class="page-item"><a class="page-link" href="${previousPages?last.url}" tabindex="-1">Previous</a></li>
                            </#if>
                            <#list previousPages as item>
                                <li class="page-item"><a class="page-link" href="${item.url}">${item.getPageNumber()}</a></li>
                            </#list>
                            <#list currentPage as item>
                                <li class="page-item active"><a class="page-link" href="${item.url}">${item.getPageNumber()}</a></li>
                            </#list>
                            <#list nextPages as item>
                                <li class="page-item"><a class="page-link" href="${item.url}">${item.getPageNumber()}</a></li>
                            </#list>
                            <#if nextPages?size == 0>
                                <li class="page-item disabled"><a class="page-link" href="#" tabindex="-1">Next</a></li>
                            <#else>
                                <li class="page-item"><a class="page-link" href="${nextPages?first.url}">Next</a></li>
                            </#if>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

    </main>

    <@m.footer />
</body>
</html>