t = int(input())

for _ in range(t):
    n,m = map(int,input().split())
    co = 0
    l = 0
    b = True
    for i in range(n):
        s= input()
        if b and len(s)+l<=m:
            co+=1
            l+= len(s)
        else:
            b = False
    print(co)
