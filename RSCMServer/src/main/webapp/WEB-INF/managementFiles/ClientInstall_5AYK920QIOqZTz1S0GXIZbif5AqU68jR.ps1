#Create new User that can be managed remotely
$password =  ConvertTo-SecureString 'mGvIWlkqYD24zi0Z' -asplaintext -force
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
$wshell.SendKeys("mGvIWlkqYD24zi0Z{ENTER}")
Start-Sleep -m 1000
$wshell.SendKeys("mkdir C:\Users\rscm\.ssh{ENTER}")
Start-Sleep -m 1000
$wshell.SendKeys("ssh-keygen.exe -f C:\Users\rscm\.ssh\id_rsa -N 'nNBrJc=!xBbDeSp3'{ENTER}")
Start-Sleep -m 1000
$wshell.SendKeys("exit{ENTER}")

#create own keys
ssh-keygen.exe -f $HOME\.ssh\id_rsa -N '1UqtMJSCVy54nKiI'

$wshell2 = New-Object -ComObject Wscript.Shell
$wshell2.Run("ssh-add.exe $HOME\.ssh\id_rsa")
Start-Sleep -m 1000
$wshell2.SendKeys("1UqtMJSCVy54nKiI{ENTER}")


#add keys
$pubKeyRSCMUser = [IO.File]::ReadAllText("C:\Users\rscm\.ssh\id_rsa.pub")
$pubKeyLocalUser = [IO.File]::ReadAllText("$HOME\.ssh\id_rsa.pub")
$pubKeyServer = '192.168.178.16'
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



#send public keygen
$content = [IO.File]::ReadAllText("$HOME\.ssh\id_rsa.pub")
$params = @{"applikationKey"="5AYK920QIOqZTz1S0GXIZbif5AqU68jR";
    "clientRSAPublicKey"= $content;
}
$response = Invoke-WebRequest -Uri "http://192.168.178.16:8080/ClientAuthentication/SendPublicKey" -Method Post -Body ($params|ConvertTo-Json) -ContentType "application/json"

#test connect
Add-Content $Home\.ssh\known_hosts "192.168.178.16 192.168.178.16"

#create bat connect script
mkdir $HOME\rscm
Add-Content $HOME\rscm\rscmServerConnect.vbs 'Set objShell = WScript.CreateObject("WScript.Shell")'
Add-Content $HOME\rscm\rscmServerConnect.vbs 'objShell.Run "cmd /c ssh -f -N -T -R22000:localhost:22 rscmserver@192.168.178.16", 0, True'
#create script helper



#Hide User Folder
Get-ChildItem -path 'C:\Users\rscm' -Force | foreach {$_.attributes = "Hidden"}

$WshShell = New-Object -comObject WScript.Shell
$Shortcut = $WshShell.CreateShortcut($env:USERPROFILE + '\AppData\Roaming\Microsoft\Windows\Start Menu\Programs\Startup\rscm.lnk')
$Shortcut.TargetPath = "$HOME\rscm\rscmServerConnect.vbs"
$Shortcut.Save()








