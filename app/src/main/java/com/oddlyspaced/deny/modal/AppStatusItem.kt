package com.oddlyspaced.deny.modal

data class AppStatusItem(var autoGrant: Boolean, var autoRevoke: Boolean, var permissions: ArrayList<PermissionItem>)