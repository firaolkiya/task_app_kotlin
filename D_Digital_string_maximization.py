t = int(input())
for _ in  range(t):
    n =list(input())
    n = list(map(int,n))
    
    ans = []
    added = 0
    for i in range(len(n)):
        
        for ind in range(min(i+8,len(n)-1),i,-1):
            if n[ind]-1>n[ind-1]:
                n[ind],n[ind-1] = n[ind-1],n[ind]-1
    n = list(map(str,n))
    
    print("".join(n))
            
    