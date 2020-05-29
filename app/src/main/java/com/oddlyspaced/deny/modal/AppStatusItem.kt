package com.oddlyspaced.deny.modal

data class AppStatusItem(val autoGrant: Boolean, val autoRevoke: Boolean, val permissions: ArrayList<PermissionItem>)