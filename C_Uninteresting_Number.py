for _ in range(int(input())):
    s = input()
    total = 0
    two = 0
    three = 0
    for char in s:
        total+=int(char)
        if char=="2":
            two+=1
        elif char=="3":
            three+=1
    
    if total%9==0:
        print("YES")
    else:
        tag = True
        for i in range(two+1):
            mod = total+i*2
            if mod%9==0 or (9-mod%9 == 3 and three>=2):
                tag=False
                print("YES")
                break
            
            elif 9-mod%9==6 and three>0:
                tag= False
                print("YES")
                break
        if tag:
            print("NO")
        
    
        
        