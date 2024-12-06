for _ in range(int(input())):
    a = input()
    b = input()
    c = input()
    
    memo = {}
    def dp(f,s,index):
        if index==len(c):
            return 0
        if not (f,s,index) in memo:
        
            left = right = float('inf')
            if f<len(a):
                left = dp(f+1,s,index+1)+int(a[f]!=c[index])
            if s<len(b):
                right = dp(f,s+1,index+1)+int(b[s]!=c[index])
            memo[(f,s,index)] = min(left,right)
        return memo[(f,s,index)]
        
    print(dp(0,0,0))
        