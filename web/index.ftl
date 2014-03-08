<!DOCTYPE html>
<html>
<head>
    <title>Sysweb</title>
</head>
<body>

<script src="/script/Library/jquery.js"></script>
<script src="/script/Library/keymap.js"></script>
<script src="/script/Library/ajax.js"></script>
<script src="/script/Library/corelib.js"></script>
<script src="/script/Library/terminal.js"></script>
<#list boots as boot>
    <${boot.getStr("tag")} ${boot.getStr("attr")}="/fs${boot.getStr("path")}"></${boot.getStr("tag")}>
</#list>
</body>
</html>