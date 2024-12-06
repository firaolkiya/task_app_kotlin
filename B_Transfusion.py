for _ in range(int(input())):
    n = int(input())
    arr = list(map(int, input().split()))
    even= 0
    odd = 0
    o,e = 0,0
    for i in range(n):
        if i%2==0:
            even+=arr[i]
            e+=1
        else:
            odd+=arr[i]
            o+=1
    if n==1:
        print("YES")
    elif odd%o!=0 or even%e!=0:
        print("NO")
    elif odd//o!=even//e:
        print("NO")
    else:
        print("YES")