import random, sys
sys.setrecursionlimit(10000)
# Python `pow` already accepts a modulus, but here's
# what you would do if it didn't:
def exp_mod(base, power, m):
	if power <= 2:
		return pow(base, power) % m
	elif power % 2 == 0:
		half_power = power // 2
		half_exp = exp_mod(base, half_power, m)
		return (half_exp * half_exp) % m
	else:
		return (base * exp_mod(base, power-1, m)) % m
# IKE group 14:
P = 0xFFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD129024E088A67CC74020BBEA63B139B22514A08798E3404DDEF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7EDEE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3DC2007CB8A163BF0598DA48361C55D39A69163FA8FD24CF5F83655D23DCA3AD961C62F356208552BB9ED529077096966D670C354E4ABC9804F1746C08CA18217C32905E462E36CE3BE39E772C180E86039B2783A2EC07A28FB5C55DF06F4C52C9DE2BCBF6955817183995497CEA956AE515D2261898FA051015728E5A8AACAA68FFFFFFFFFFFFFFFF
g = 2
secret_a = random.randrange(P-1)+1
secret_b = random.randrange(P-1)+1
mod_secret_A = exp_mod(g, secret_a, P)
mod_secret_B = exp_mod(g, secret_b, P)
x_a = exp_mod(mod_secret_A, secret_b, P)
x_b = exp_mod(mod_secret_B, secret_a, P)
assert(x_a == x_b)
print("shared secret was " + str(x_a))