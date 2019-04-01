import firebase_admin
from firebase_admin import db, credentials


cred = credentials.Certificate('E:\Technion\semester 8\scripts\\team5fooddb.json')  #~~~ CHANGE HERE
firebase_admin = firebase_admin.initialize_app(cred, {'databaseURL': 'https://team5fooddb.firebaseio.com'})
ref = db.reference('')
foods_ref = ref.child('food')

food = input()

while(food != "stop"):
    if len(food) < 2:
        print("food must contain 2 or more characters\n")
    splitted=food.split(" ")
    if(len(splitted)==1):
        snapshot = foods_ref.order_by_key().start_at(food.upper()).limit_to_first(20).get()
        for key in snapshot:
            print(key)
        print("done printing for "+food)
    else:
        snapshot=foods_ref.child(food).get()
        for nut in snapshot:
            print(nut+": "+str(snapshot[nut]))
        print("done printing for " + food)
    food = input()