var r = exec('dir');
console.log('result of "dir"', JSON.stringify(r));
if(r.output.includes('README.md')) {
	console.log('found README.md')
} 
exec('set greeting=HELLOééé');
exec('echo greeting=%greeting%');
r = {a:1, b:2};
r;