def main():
    Line(30,30,80,30)
    print("ieofjof")
    
def swap(a,b):
    t = a
    a = b
    b = t

def plot(x,y):
    print(y,x)

def Line (x0, y0, x1, y1):
    if (abs(y1 - y0) > abs(x1 - x0)):
        st = 1
    else:
        st = 0

    if st == 1:
        swap(x0, y0)
        swap(x1, y1)
 
    if x0 > x1:
        swap(x0, x1)
        swap(y0, y1)

    deltax = x1 - x0
    deltay = abs(y1 - y0)
    error = 0
    y = y0
 
    if y0 < y1:
        ystep = 1
    else:
        ystep = -1
 
    for x in range(x0,x1):
        if st == 1:
            plot(y,x)
        else:
            plot(x,y)

        error = error + deltay

        if 2*error >= deltax:
            y = y + ystep
            error = error - deltax
            
if __name__ == "__main__":
    main()

